package com.welovecoding.web.blog.markdown.meta;

import static com.welovecoding.web.blog.markdown.meta.MarkdownMetaParser.META_END;
import static com.welovecoding.web.blog.markdown.meta.MarkdownMetaParser.META_START;
import static com.welovecoding.web.blog.markdown.meta.MarkdownMetaParser.KEY_VALUE_SEPARATOR;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BufferedMarkdownMetaParser {

  private Map<String, MarkdownMetaData> data;
  private boolean startReading;

  public BufferedMarkdownMetaParser() {
    this.data = new HashMap<>();
    this.startReading = false;
  }

  public void parse(String line) {
    switch (line) {
      case META_START:
        startReading = true;
        break;
      case META_END:
        startReading = false;
        break;
    }

    if (startReading == true) {
      parseData(line);
    }

  }

  private void parseData(String meta) {
    String[] keyValuePair = meta.trim().split(KEY_VALUE_SEPARATOR);
    String key = null;
    String value = "";

    if (keyValuePair.length > 1) {
      key = keyValuePair[0].trim();
      value = keyValuePair[1].trim().replaceAll("'", "\"");
    }

    if (isArray(value)) {
      System.out.println("IS ARRAY");
      String[] values = convertValueArray(value);
      data.put(key, new MarkdownMetaData(key, values));
    } else {
      data.put(key, new MarkdownMetaData(key, new String[]{value}));
    }

  }

  private boolean isArray(String value) {
    return (value.startsWith("[") && value.endsWith("]"));
  }

  private String[] convertValueArray(String valueArray) {
    String values = valueArray.substring(1, valueArray.length() - 1);
    String[] splitted = values.split(",");
    String[] results = new String[splitted.length];
    System.out.println(splitted.length);

    for (int i = 0; i < results.length; i++) {
      results[i] = splitted[i].replace("\"", "").trim();
    }

    return results;
  }

  public void print() {
    System.out.println("Items: " + data.size());

    for (String key : data.keySet()) {
      MarkdownMetaData keyValuePair = data.get(key);
      System.out.println("Key: " + keyValuePair.getKey());
      System.out.println("Value(s):");
      for (String value : keyValuePair.getValues()) {
        System.out.println(value);
      }
    }
  }

  public Map<String, MarkdownMetaData> getData() {
    return data;
  }

  public void setData(Map<String, MarkdownMetaData> data) {
    this.data = data;
  }

}
