package com.welovecoding.web.blog.github;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebhookMapper {

  public WebhookMapper() {
  }

  public WebhookInfo map(String payload) {
    JSONObject jsonObject = new JSONObject(payload);
    return map(jsonObject);
  }

  public WebhookInfo map(JSONObject json) {

    WebhookInfo info = new WebhookInfo();

    info.setCredential(parseCommitterEmail(json));
    info.setLocalRepositoryPath(createLocalRepositoryPath(json));
    info.setModifiedFiles(parseModifiedFiles(json));
    info.setMovedFiles(new ArrayList<String>());
    info.setReference(parseReference(json));
    info.setRemovedFiles(parseRemovedFiles(json));
    info.setRepositoryName(parseRepositoryName(json));
    info.setRepositoryUrl(parseRepositoryUrl(json));
    info.setUntrackedFiles(parseAddedFiles(json));

    return info;

  }

  protected String createLocalRepositoryPath(JSONObject json) {
    String home = System.getProperty("user.home");
    String name = parseRepositoryName(json);
    File path = new File(home + System.getProperty("file.separator") + name);
    return path.getAbsolutePath();
  }

  private String parseRepositoryName(JSONObject json) {
    JSONObject repository = json.getJSONObject("repository");
    return repository.get("name").toString();
  }

  private String parseRepositoryUrl(JSONObject json) {
    JSONObject repository = json.getJSONObject("repository");
    return repository.get("url").toString();
  }

  private String parseReference(JSONObject json) {
    return json.get("ref").toString();
  }

  private String parseCommitterEmail(JSONObject json) {
    JSONArray commits = json.getJSONArray("commits");
    JSONObject commitInfo = commits.getJSONObject(0);
    JSONObject committer = commitInfo.getJSONObject("author");
    Object email = committer.get("email");

    return email.toString();
  }

  private List<String> parseModifiedFiles(JSONObject json) {
    return parseFiles(json, "modified");
  }

  private List<String> parseAddedFiles(JSONObject json) {
    return parseFiles(json, "added");
  }

  private List<String> parseRemovedFiles(JSONObject json) {
    return parseFiles(json, "removed");
  }

  private List<String> parseFiles(JSONObject json, String attribute) {
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
