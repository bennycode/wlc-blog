package com.welovecoding.web.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/articles/*")
public class ArticleFilter implements Filter {

  private static final Logger LOG = Logger.getLogger(ArticleFilter.class.getName());

  public ArticleFilter() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    HttpServletRequest servletRequest = (HttpServletRequest) request;
    HttpServletResponse servletResponse = (HttpServletResponse) response;

    String contextPath = servletRequest.getContextPath();
    String requestURI = servletRequest.getRequestURI();
    String path = requestURI.substring(contextPath.length(), requestURI.length());
    String resourcePath = path + ".md";

    LOG.log(Level.INFO, "Requested path: {0}", path);
    InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);

    if (is == null) {
      LOG.log(Level.WARNING, "File could not be found: {0}", resourcePath);
    } else {
      LOG.log(Level.INFO, "Found file: {0}", resourcePath);

      String currentLine;

      try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        while ((currentLine = br.readLine()) != null) {
          System.out.println(currentLine);
        }
      } catch (IOException ex) {
        LOG.log(Level.SEVERE, ex.getMessage());
      }

    }

    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
  }

  @Override
  public void destroy() {
  }
}
