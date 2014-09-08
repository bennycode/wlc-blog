package com.welovecoding.web.blog.github;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class WebhookMapperTest {

  public WebhookMapperTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  @Ignore
  public void testMapping() throws Exception {
    // Test file
    String fileName = "github-webhook-6465785245014415509.json";
    String payload;
    try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
      payload = IOUtils.toString(in);
    }

    // Mapping
    WebhookMapper mapper = new WebhookMapper();
    WebhookInfo info = mapper.map(payload);

    // Verification
    WebhookInfo expected = new WebhookInfo();
    expected.setCredential("bn@bennyn.de");
    // expected.setLocalRepositoryPath(mapper.createLocalRepositoryPath());
    expected.setModifiedFiles(Arrays.asList("src/main/java/com/welovecoding/web/servlet/WebHookServlet.java"));
    expected.setMovedFiles(new ArrayList<String>());
    expected.setReference("refs/heads/master");
    expected.setRemovedFiles(new ArrayList<String>());
    expected.setRepositoryName("wlc-blog");
    expected.setRepositoryUrl("https://github.com/bennyn/wlc-blog");
    expected.setUntrackedFiles(new ArrayList<String>());

    assertEquals(expected, info);
  }

}
