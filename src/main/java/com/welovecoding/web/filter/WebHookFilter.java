package com.welovecoding.web.filter;

import com.welovecoding.web.util.RequestPrinter;
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

@WebFilter("/webhook/*")
public class WebHookFilter implements Filter {

  private static final Logger LOG = Logger.getLogger(WebHookFilter.class.getName());

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest servletRequest = (HttpServletRequest) request;
    HttpServletResponse servletResponse = (HttpServletResponse) response;

    String userAgent = servletRequest.getHeader("user-agent");
    String event = servletRequest.getHeader("x-github-event");

    if (userAgent.contains("GitHub-Hookshot") && event.equals("push")) {
      String payload = servletRequest.getParameter("payload");
      System.out.println("GITHUB PAYLOAD: " + payload);
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
