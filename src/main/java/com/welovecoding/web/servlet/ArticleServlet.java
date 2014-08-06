package com.welovecoding.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ArticleServlet", urlPatterns = {"/articles/*"})
public class ArticleServlet extends HttpServlet {

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");

    String contextPath = request.getContextPath();
    String requestURI = request.getRequestURI();
    String path = requestURI.substring(contextPath.length(), requestURI.length());
    String resourcePath = path + ".md";

    InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);

    if (is != null) {
      String currentLine;

      try (PrintWriter out = response.getWriter(); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Article</title>");
        out.println("</head>");
        out.println("<body>");

        while ((currentLine = br.readLine()) != null) {
          out.println(currentLine);
        }

        out.println("</body>");
        out.println("</html>");
      }

    } else {

      response.sendRedirect(request.getContextPath());

    }

  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>

}
