package com.welovecoding.web.blog.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * http://stackoverflow.com/a/6037368/451634
 * http://johannesbrodwall.com/2009/10/24/testing-servlets-with-mockito/
 *
 * @author Benny
 */
public class ArticleServletTest {

  @Test
  public void testBuildResourcePath() throws Exception {
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpServletRequest request = mock(HttpServletRequest.class);

    PrintWriter writer = new PrintWriter(new StringWriter());
    when(response.getWriter()).thenReturn(writer);

    TestServlet testServlet = new TestServlet();
    testServlet.doPost(request, response);

    verify(response).setContentType("text/html;charset=UTF-8");
  }

}
