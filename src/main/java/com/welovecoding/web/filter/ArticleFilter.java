package com.welovecoding.web.filter;

import java.io.IOException;
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

    LOG.log(Level.INFO, "Requested path: {0}", path);

    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
  }

  @Override
  public void destroy() {
  }
}
