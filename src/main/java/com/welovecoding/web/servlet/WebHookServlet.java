package com.welovecoding.web.servlet;

import com.welovecoding.web.util.GitHubUtility;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "WebHookServlet", urlPatterns = {"/webhook/*"})
public class WebHookServlet extends HttpServlet {

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
      String payload = servletRequest.getParameter("payload");
      String privateKey = "abc123";

      String hash = GitHubUtility.hash_hmac(payload, privateKey);
      System.out.println("Hash: " + hash);
      System.out.println("Verify: "+servletRequest.getHeader("x-hub-signature"));

      // System.out.println("GITHUB PAYLOAD: " + payload);
    }

    processRequest(servletRequest, response);
  }

}
