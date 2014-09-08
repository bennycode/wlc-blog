package com.welovecoding.web.blog.servlet;

import com.welovecoding.web.blog.domain.article.Article;
import com.welovecoding.web.blog.domain.article.ArticleMapper;
import com.welovecoding.web.blog.domain.article.ArticleService;
import com.welovecoding.web.blog.git.GitHubController;
import com.welovecoding.web.blog.github.WebhookInfo;
import com.welovecoding.web.blog.github.WebhookMapper;
import com.welovecoding.web.blog.settings.Settings;
import com.welovecoding.web.blog.util.CryptographyUtility;
import com.welovecoding.web.blog.util.FileUtility;
import com.welovecoding.web.blog.util.RequestPrinter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "WebhookServlet", urlPatterns = {"/webhook/*"})
public class WebhookServlet extends HttpServlet {

  private static final Logger LOG = Logger.getLogger(WebhookServlet.class.getName());
  private final GitHubController gitHubController = new GitHubController();
  private final ArticleMapper articleMapper = new ArticleMapper();
  private final WebhookMapper mapper = new WebhookMapper();
  private String payload;
  @EJB
  private ArticleService articleService;

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");

    try (PrintWriter out = response.getWriter()) {
      printPositiveResponse(out);
    } catch (Exception ex) {
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
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String debugString = RequestPrinter.debugString(request);
    System.out.println("Headers: ");
    System.out.println(debugString);

    boolean isPushCommit = checkForPushCommit(request);

    if (isPushCommit) {
      handlePayload(request);
    }

    processRequest(request, response);
  }

  private boolean checkForPushCommit(HttpServletRequest request) {
    boolean isPushCommit = false;

    String userAgent = request.getHeader("user-agent");
    String event = request.getHeader("x-github-event");

    if (userAgent.contains("GitHub-Hookshot") && event.equals("push")) {
      isPushCommit = true;
    } else {
      LOG.log(Level.INFO, "There was a request from a service other than GitHub.");
    }

    return isPushCommit;
  }

  private void handlePayload(HttpServletRequest request) {
    boolean isValidPayload = validatePayload(request);

    if (isValidPayload) {
      LOG.log(Level.INFO, "Valid GitHub Webhook Payload.");
      System.out.println("Output: " + payload);

      // Process Payload
      WebhookInfo info = mapper.map(payload);

      // Pull files in Git
      boolean isPulled = gitHubController.pullFiles(info);

      if (isPulled) {
        String repositoryPath = info.getLocalRepositoryPath();
        for (String filePath : info.getModifiedFiles()) {
          String absoluteFilePath = FileUtility.joinDirectoryAndFilePath(repositoryPath, filePath);
          Article article = articleMapper.mapArticleFromMarkdownFile(absoluteFilePath);

          if (article != null) {
            article.setId(filePath);
            articleService.save(article);
          }

        }
      }

      // Parse files in Git
      // Write information to database
    } else {
      LOG.log(Level.WARNING, "Invalid GitHub Webhook Payload.");
    }

  }

  private boolean validatePayload(HttpServletRequest request) {
    readPayload(request);
    String signature = request.getHeader("x-hub-signature");

    // writePayloadToTempFile(payload);
    return CryptographyUtility.verifySignature(payload, signature, Settings.WEBHOOK_SECRET);
  }

  private void writePayloadToTempFile(String payload) {
    try {
      File file = File.createTempFile("github-webhook-", ".json");

      try (FileWriter writer = new FileWriter(file)) {
        writer.write(payload);
      }

      LOG.log(Level.INFO, "Wrote file to: {0}", file.getAbsolutePath());
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Error writing file: {0}", ex.getMessage());
    }
  }

  private String readPayload(HttpServletRequest request) {
    this.payload = "";

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
