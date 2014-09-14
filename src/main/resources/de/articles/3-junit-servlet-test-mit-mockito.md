**TestServlet.java**
```java
package com.welovecoding.web.blog.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TestServlet", urlPatterns = {"/test"})
public class TestServlet extends HttpServlet {

  protected void processRequest(HttpServletRequest request,
          HttpServletResponse response)
          throws ServletException, IOException {

    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servlet TestServlet</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>TestServlet</h1>");
      out.println("</body>");
      out.println("</html>");
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

}

```

**TestServletTest.java**
```java
package com.welovecoding.web.blog.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TestServletTest {

  public TestServletTest() {
  }

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

```

**pom.xml**
```xml
<!-- Servlet API (needed for Mockito) -->
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>javax.servlet-api</artifactId>
  <version>3.1.0</version>
</dependency>
...
<!-- Mockito -->
<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-all</artifactId>
  <version>1.9.5</version>
  <scope>test</scope>
  <type>jar</type>
</dependency>
```