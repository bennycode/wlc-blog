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
  private static final String WEBHOOK_SECRET = "abc123";
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    
    try (PrintWriter out = response.getWriter()) {
      printPositiveResponse(out);
    } catch(Exception ex) {
      logError(ex);
    }
    
  }
  
  private void printPositiveResponse(PrintWriter out) {
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
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    System.out.println("1");
    boolean isPushCommit = checkForPushCommit(request);
    
    if (isPushCommit) {
      handlePayload(request);
    }
    
    System.out.println("JA");
    processRequest(request, response);
  }
  
  private boolean checkForPushCommit(HttpServletRequest request) {
    boolean isPushCommit = false;
    
    String userAgent = request.getHeader("user-agent");
    String event = request.getHeader("x-github-event");
    
    if (userAgent.contains("GitHub-Hookshot") && event.equals("push")) {
      isPushCommit = true;
    }
    
    return isPushCommit;
  }
  
  private void handlePayload(HttpServletRequest request) {
    boolean isValidPayload = validatePayload(request);
    
    if (isValidPayload) {
      LOG.log(Level.INFO, "Valid GitHub Webhook Payload.");
    } else {
      LOG.log(Level.WARNING, "Invalid GitHub Webhook Payload.");
    }
  }
  
  private boolean validatePayload(HttpServletRequest request) {
    String payload = readPayload(request);
    String signature = request.getHeader("x-hub-signature");
    
    return GitHubUtility.verifySignature(payload, signature, WEBHOOK_SECRET);
  }
  
  private String readPayload(HttpServletRequest request) {
    String payload = "";
    
    try {
      payload = getRequestBody(request);
    } catch (IOException ex) {
      logError(ex);
    }
    
    return payload;
  }
  
  private String getRequestBody(HttpServletRequest request) throws IOException {
    StringBuilder sb = new StringBuilder();
    String line;
    
    BufferedReader reader = request.getReader();
    while ((line = reader.readLine()) != null) {
      sb.append(line);
    }
    
    return sb.toString();
  }
  
  private void logError(Exception ex) {
    LOG.log(Level.WARNING, ex.getLocalizedMessage());
  }
  
}
