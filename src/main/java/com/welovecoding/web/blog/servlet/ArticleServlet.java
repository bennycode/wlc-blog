package com.welovecoding.web.blog.servlet;

import com.welovecoding.web.blog.domain.article.Article;
import com.welovecoding.web.blog.domain.article.ArticleService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ArticleServlet", urlPatterns = {"/articles/*"})
public class ArticleServlet extends HttpServlet {

  private static final Logger LOG = Logger.getLogger(ArticleServlet.class.getName());
  @EJB
  private ArticleService articleService;

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    String requestURI = request.getRequestURI(); // /wlc-blog/articles/parent-selectors-in-less
    String slug = requestURI.substring(requestURI.lastIndexOf("/") + 1); // parent-selectors-in-less
    String languageCode = Locale.GERMAN.getLanguage(); // de

    Article article = articleService.getByLanguageCodeAndSlug(languageCode, slug);

    if (article != null) {
      renderHtml(response, article);
    } else {
      response.sendError(404);
    }

  }

  private static void renderHtml(HttpServletResponse response, Article article) {
    response.setContentType("text/html;charset=UTF-8");

    try (PrintWriter out = response.getWriter()) {
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("</head>");
      out.println("<body>");
      out.println(article.getHtml());
      out.println("</body>");
      out.println("</html>");
    } catch (IOException ex) {
      LOG.log(Level.WARNING, ex.getMessage());
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
  }

}
