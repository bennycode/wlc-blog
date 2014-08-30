package com.welovecoding.web.blog.github;

import java.util.List;

public class WebhookInfo {

  private String reference = "refs/heads/master";
  private String credential = "max.mustermann@welovecoding.com";
  private String remoteUrl = "https://github.com/bennyn/wlc-blog";
  private String localRepository = "C:\\Users\\Benny\\wlc-blog";

  private List<String> modifiedFiles;
  private List<String> deletedFiles;
  private List<String> newFiles;

  public WebhookInfo() {
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getCredential() {
    return credential;
  }

  public void setCredential(String credential) {
    this.credential = credential;
  }

  public String getRemoteUrl() {
    return remoteUrl;
  }

  public void setRemoteUrl(String remoteUrl) {
    this.remoteUrl = remoteUrl;
  }

  public String getLocalRepository() {
    return localRepository;
  }

  public void setLocalRepository(String localRepository) {
    this.localRepository = localRepository;
  }

  public List<String> getModifiedFiles() {
    return modifiedFiles;
  }

  public void setModifiedFiles(List<String> modifiedFiles) {
    this.modifiedFiles = modifiedFiles;
  }

  public List<String> getDeletedFiles() {
    return deletedFiles;
  }

  public void setDeletedFiles(List<String> deletedFiles) {
    this.deletedFiles = deletedFiles;
  }

  public List<String> getNewFiles() {
    return newFiles;
  }

  public void setNewFiles(List<String> newFiles) {
    this.newFiles = newFiles;
  }

}
