package com.welovecoding.web.blog.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * http://stackoverflow.com/a/6037368/451634
 * http://johannesbrodwall.com/2009/10/24/testing-servlets-with-mockito/
 *
 * @author Benny
 */
public class ArticleServletTest {

  @Test
  public void testBuildResourcePath() {
    String contextPath = "/wlc-blog";
    String requestURI = "/wlc-blog/articles/test";
    String resourcePath = ArticleServlet.buildResourcePath(contextPath, requestURI);
    assertEquals("/articles/test.md", resourcePath);
  }

}
