package com.welovecoding.web.servlet;

import com.welovecoding.web.util.GitHubUtility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "WebHookServlet", urlPatterns = {"/webhook/*"})
public class WebHookServlet extends HttpServlet {

  private static final Logger LOG = Logger.getLogger(WebHookServlet.class.getName());

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");

    try (PrintWriter out = response.getWriter()) {
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("<title>WebHookServlet</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<p>OK</p>");
      out.println("</body>");
      out.println("</html>");
    }
  }

  @Override
  protected void doPost(HttpServletRequest servletRequest, HttpServletResponse response)
          throws ServletException, IOException {
    String userAgent = servletRequest.getHeader("user-agent");
    String event = servletRequest.getHeader("x-github-event");

    // http://isometriks.com/verify-github-webhooks-with-php
    if (userAgent.contains("GitHub-Hookshot") && event.equals("push")) {
      String signature = servletRequest.getHeader("x-hub-signature");
      String secret = "abc123";

      // Read body
      StringBuilder jb = new StringBuilder();
      String line;
      try {
        BufferedReader reader = servletRequest.getReader();
        while ((line = reader.readLine()) != null) {
          jb.append(line);
        }
      } catch (IOException ex) {
        //
      }

      String payload = jb.toString();
      LOG.log(Level.INFO, "Verified GitHub Webhook: {0}",
              GitHubUtility.verifySignature(payload, signature, secret));
    }

    processRequest(servletRequest, response);
  }

}
