package com.welovecoding.util.markdown.meta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Inspired by https://github.com/lichunr/markdown-meta.
 *
 * @author Benny
 */
public class MarkdownMetaParser {

  private static final Logger LOG = Logger.getLogger(MarkdownMetaParser.class.getName());

  public static final String META_START = "<!--*";
  public static final String META_END = "*-->";
  private static final String META_SEPARATOR = ";";
  private static final String KEY_VALUE_SEPARATOR = ":";

  public Map<String, MarkdownMetaData> parseStream(InputStream stream) {
    String metaDataString = readFileContent(stream);
    Map<String, MarkdownMetaData> extractedMetaData = extractMetadata(metaDataString);

    return extractedMetaData;
  }

  public Map<String, MarkdownMetaData> extractMetadata(String metaData) {
    String[] plainMetadatas = metaData.split(META_SEPARATOR);
    Map<String, MarkdownMetaData> resultList = new HashMap<>();

    for (String meta : plainMetadatas) {
      String[] pairs = meta.trim().split(KEY_VALUE_SEPARATOR);

      if (pairs.length != 2) {
        continue;
      }

      String key = pairs[0].trim();
      String value = pairs[1].trim().replaceAll("'", "\"");

      if (isStringifiedArray(value)) {
        String[] values = convertStringifiedArray(value);
        resultList.put(key, new MarkdownMetaData(key, values));
      } else {
        resultList.put(key, new MarkdownMetaData(key, new String[]{value}));
      }

    }

    return resultList;
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
    boolean startReading = false;

    // try-with-resources
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      while ((line = reader.readLine()) != null) {
        if (line.equals(META_END)) {
          break;
        }

        if (startReading == true) {
          sb.append(line);
          sb.append(System.getProperty("line.separator", "\r\n"));
        }

        if (line.equals(META_START)) {
          startReading = true;
        }
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
