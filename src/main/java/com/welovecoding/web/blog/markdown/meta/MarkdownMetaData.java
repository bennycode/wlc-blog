package com.welovecoding.web.blog.markdown.meta;

public class MarkdownMetaData {

  private String key;
  private String[] values;

  public MarkdownMetaData() {
  }

  public MarkdownMetaData(String key, String[] values) {
    this.key = key;
    this.values = values;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String[] getValues() {
    return values;
  }

  public void setValues(String[] values) {
    this.values = values;
  }

}
