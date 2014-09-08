package com.welovecoding.web.blog.servlet;

import com.welovecoding.web.blog.domain.article.Article;
import com.welovecoding.web.blog.domain.article.ArticleService;
import com.welovecoding.web.blog.markdown.meta.MarkdownMetaData;
import com.welovecoding.web.blog.markdown.meta.MarkdownMetaParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.markdown4j.Markdown4jProcessor;

@WebServlet(name = "ArticleServlet", urlPatterns = {"/articles/*"})
public class ArticleServlet extends HttpServlet {

  private static final Logger LOG = Logger.getLogger(ArticleServlet.class.getName());
  private static String title = "";
  private static String description = "";
  private static String content = "";
  @EJB
  private ArticleService articleService;

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    // Key in DB: src/main/resources/articles/test.md
    // Resource Path Example: /articles/test.md
    String resourcePath = buildResourcePath(request);
    String primaryKey = "src/main/resources" + resourcePath;

    Article article = articleService.findById(primaryKey);
    if (article != null) {
      renderHtml(request, response, article.getHtml());
    } else {
      renderHtml(request, response, "404");
    }

  }

  private static void renderHtml(HttpServletRequest request, HttpServletResponse response, String code) {
    response.setContentType("text/html;charset=UTF-8");
    String resourcePath = buildResourcePath(request);

    try (PrintWriter out = response.getWriter()) {
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("</head>");
      out.println("<body>");
      out.println(resourcePath);
      out.println("</body>");
      out.println("</html>");
    } catch (IOException ex) {
      LOG.log(Level.WARNING, ex.getMessage());
    }
  }

  private static void printResponse(PrintWriter out) {
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head>");
    out.println("  <title>" + title + "</title>");
    out.println("  <meta name=\"description\" content=\"" + description + "\" />");
    out.println("</head>");
    out.println("<body>");
    out.println(content);
    out.println("</body>");
    out.println("</html>");
  }

  private static void processResource(InputStream is) {
    String fileContent = readFileContent(is);

    // Markdown meta
    Map<String, MarkdownMetaData> extractedMetaData = readMetaData(fileContent);
    processMetaData(extractedMetaData);

    // Markdown
    String markdown = readMarkdown(fileContent);

    try {
      content = new Markdown4jProcessor().process(markdown);
    } catch (IOException ex) {
      LOG.log(Level.WARNING, ex.getMessage());
    }
  }

  private static Map<String, MarkdownMetaData> readMetaData(String content) {
    String metaData = splitMetaData(content);
    MarkdownMetaParser mp = new MarkdownMetaParser();
    Map<String, MarkdownMetaData> extractedMetaData = mp.extractMetadata(metaData);

    return extractedMetaData;
  }

  private static String readMarkdown(String content) {
    int start = content.indexOf(MarkdownMetaParser.META_END) + MarkdownMetaParser.META_END.length();
    return content.substring(start);
  }

  private static void processMetaData(Map<String, MarkdownMetaData> data) {
    title = data.get("title").getValues()[0];
    description = data.get("description").getValues()[0];
  }

  private static String readFileContent(InputStream is) {
    String result = "";

    try {
      result = IOUtils.toString(is, "UTF-8");
    } catch (IOException ex) {
      LOG.log(Level.WARNING, ex.getMessage());
    }

    return result;
  }

  private static String splitMetaData(String content) {
    int start = content.indexOf(MarkdownMetaParser.META_START) + MarkdownMetaParser.META_START.length();
    int end = content.indexOf(MarkdownMetaParser.META_END);
    String result = content.substring(start, end);

    return result;
  }

  private static String buildResourcePath(HttpServletRequest request) {
    String contextPath = request.getContextPath();
    String requestURI = request.getRequestURI();
    String path = requestURI.substring(contextPath.length(), requestURI.length());
    String resourcePath = path + ".md";

    return resourcePath;
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
