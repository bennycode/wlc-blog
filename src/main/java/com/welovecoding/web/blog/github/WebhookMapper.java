package com.welovecoding.web.blog.github;

import com.welovecoding.web.blog.util.FileUtility;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebhookMapper {

  WebhookInfo info;

  public WebhookMapper() {
  }

  public WebhookInfo map(String payload) {
    JSONObject jsonObject = new JSONObject(payload);
    return map(jsonObject);
  }

  public WebhookInfo map(JSONObject json) {
    info = new WebhookInfo();

    // Meta 
    String repositoryPath = createLocalRepositoryPath(json);
    info.setLocalRepositoryPath(repositoryPath);
    info.setCredential(parseCommitterEmail(json));
    info.setReference(parseReference(json));
    info.setRepositoryName(parseRepositoryName(json));
    info.setRepositoryUrl(parseRepositoryUrl(json));

    // Files
    List<String> modifiedFiles = parseModifiedFiles(json);
    addFiles(modifiedFiles, RepositoryFileStatus.MODIFIED);

    List<String> removedFiles = parseRemovedFiles(json);
    addFiles(removedFiles, RepositoryFileStatus.DELETED);

    List<String> untrackedFiles = parseAddedFiles(json);
    addFiles(untrackedFiles, RepositoryFileStatus.ADDED);

    return info;
  }

  private void addFiles(List<String> filePaths, RepositoryFileStatus status) {
    Map<String, RepositoryFile> files = info.getFiles();
    RepositoryFile file = new RepositoryFile();

    for (String filePath : filePaths) {
      file.setRelativePath(filePath);
      file.setAbsoluteLocalPath(FileUtility.joinDirectoryAndFilePath(info.getLocalRepositoryPath(), filePath));
      file.setStatus(status);

      files.put(filePath, file);
    }
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
