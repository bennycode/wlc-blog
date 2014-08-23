package com.welovecoding.web.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarkdownMetaParser {

  private static final Logger LOG = Logger.getLogger(MarkdownMetaParser.class.getName());

  private static final String META_START = "<!--*";
  private static final String META_END = "*-->";
  private static final String META_SEPARATOR = ";";
  private static final String KEY_VALUE_SEPARATOR = ":";
  private static final String LINE_BREAK = System.getProperty("line.separator", "\r\n");

  public void parseStream(InputStream stream) {
    String content = readFileContent(stream);
    String metaData = parseMetadata(content);
    extractMetadata(metaData);
  }

  public String parseMetadata(String content) {
    int start = content.indexOf(META_START) + META_START.length();
    int end = content.indexOf(META_END, start);

    return content.substring(start, end);
  }

  private void extractMetadata(String metaData) {
    String[] plainMetadatas = metaData.split(META_SEPARATOR);
    System.out.println(plainMetadatas.length);

    for (String meta : plainMetadatas) {
      String[] pairs = meta.trim().split(KEY_VALUE_SEPARATOR);

      if (pairs.length != 2) {
        continue;
      }

      String key = pairs[0].trim();
      String value = pairs[1].trim().replaceAll("'", "\"");

      System.out.println("Key: " + key);
      System.out.println("Value: " + value);

      if (isStringifiedArray(value)) {
        String[] values = convertStringifiedArray(value);
        for (String result : values) {
          System.out.println("BWAH: " + result);
        }
      }
    }
  }

  private String[] convertStringifiedArray(String value) {
    String values = value.substring(1, value.length() - 1);
    String[] splitted = values.split(",");
    String[] results = new String[splitted.length];

    for (int i = 0; i < results.length; i++) {
      results[i] = splitted[i].replace("\"", "").trim();
    }

    return results;
  }

  private boolean isStringifiedArray(String value) {
    return (value.startsWith("[") && value.endsWith("]"));
  }

  private String readFileContent(InputStream stream) {
    StringBuilder sb = new StringBuilder();
    String line;

    // try-with-resources
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      while ((line = reader.readLine()) != null) {
        sb.append(line);
        sb.append(System.getProperty("line.separator", "\r\n"));
      }
    } catch (IOException ex) {
      logError(ex);
    }

    return sb.toString();
  }

  private void logError(Exception ex) {
    LOG.log(Level.WARNING, ex.getLocalizedMessage());
  }

}
