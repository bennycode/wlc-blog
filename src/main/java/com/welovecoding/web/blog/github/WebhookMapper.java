package com.welovecoding.web.blog.github;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebhookMapper {

  private JSONObject json;
  private WebhookInfo info;

  public WebhookMapper(String payload) {
    this(new JSONObject(payload));
  }

  public WebhookMapper(JSONObject json) {
    this.json = json;
    this.info = new WebhookInfo();
  }

  public void map() {

    String reference = parseReference();
    String credential = parseCommitterEmail();

    List<String> modifiedFiles = parseModifiedFiles();

    System.out.println(modifiedFiles.get(0));
  }

  private String parseReference() {
    return json.get("ref").toString();
  }

  private String parseCommitterEmail() {
    JSONArray commits = json.getJSONArray("commits");
    JSONObject commitInfo = commits.getJSONObject(0);
    JSONObject committer = commitInfo.getJSONObject("author");
    Object email = committer.get("email");

    return email.toString();
  }

  private List<String> parseModifiedFiles() {
    JSONArray commits = json.getJSONArray("commits");
    JSONObject commitInfo = commits.getJSONObject(0);
    JSONArray modified = commitInfo.getJSONArray("modified");
    return convertIntoList(modified);
  }

  private List<String> convertIntoList(JSONArray jsonArray) {
    ArrayList<String> arrayList = new ArrayList<>(jsonArray.length());

    for (int i = 0; i < jsonArray.length(); i++) {
      String value = String.valueOf(jsonArray.opt(i));
      arrayList.add(value);
    }

    return arrayList;
  }

}
