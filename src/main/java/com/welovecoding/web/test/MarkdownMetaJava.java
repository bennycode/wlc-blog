package com.welovecoding.web.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarkdownMetaJava {

  private static final Logger LOG = Logger.getLogger(MarkdownMetaJava.class.getName());

  // File path: src\main\resources\articles\test.md
  public static void main(String[] args) {
    String fileSeparator = System.getProperty("file.separator ", "/");
    String filePath = "articles" + fileSeparator + "test.md";

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream stream = classLoader.getResourceAsStream(filePath);

    MarkdownMetaParser mp = new MarkdownMetaParser();

    if (stream != null) {
      LOG.log(Level.INFO, "File found: {0}", filePath);
      mp.parseStream(stream);
    } else {
      LOG.log(Level.WARNING, "File could not be found: {0}", filePath);
    }
  }

}
