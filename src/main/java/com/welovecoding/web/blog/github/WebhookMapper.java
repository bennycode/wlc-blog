package com.welovecoding.web.blog.github;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebhookMapper {

  private JSONObject json;

  public WebhookMapper(String payload) {
    this(new JSONObject(payload));
  }

  public WebhookMapper(JSONObject json) {
    this.json = json;
  }

  public WebhookInfo map() {

    WebhookInfo info = new WebhookInfo();

    info.setCredential(parseCommitterEmail());
    info.setLocalRepositoryPath(createLocalRepositoryPath());
    info.setModifiedFiles(parseModifiedFiles());
    info.setMovedFiles(new ArrayList<String>());
    info.setReference(parseReference());
    info.setRemovedFiles(parseRemovedFiles());
    info.setRepositoryName(parseRepositoryName());
    info.setRepositoryUrl(parseRepositoryUrl());
    info.setUntrackedFiles(parseAddedFiles());

    return info;

  }

  protected String createLocalRepositoryPath() {
    String home = System.getProperty("user.home");
    String name = parseRepositoryName();
    File path = new File(home + System.getProperty("file.separator") + name);
    return path.getAbsolutePath();
  }

  private String parseRepositoryName() {
    JSONObject repository = json.getJSONObject("repository");
    return repository.get("name").toString();
  }

  private String parseRepositoryUrl() {
    JSONObject repository = json.getJSONObject("repository");
    return repository.get("url").toString();
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
    return parseFiles("modified");
  }

  private List<String> parseAddedFiles() {
    return parseFiles("added");
  }

  private List<String> parseRemovedFiles() {
    return parseFiles("removed");
  }

  private List<String> parseFiles(String attribute) {
    JSONArray commits = json.getJSONArray("commits");
    JSONObject commitInfo = commits.getJSONObject(0);
    JSONArray modified = commitInfo.getJSONArray(attribute);
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
