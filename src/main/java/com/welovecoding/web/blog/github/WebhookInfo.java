package com.welovecoding.web.blog.github;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WebhookInfo {

  private String reference = "refs/heads/master";
  private String credential = "max.mustermann@welovecoding.com";
  private String repositoryName = "wlc-blog";
  private String repositoryUrl = "https://github.com/bennyn/wlc-blog";
  private String localRepositoryPath = "C:\\Users\\Benny\\wlc-blog";
  private Map<String, RepositoryFile> files;

  public WebhookInfo() {
    this.files = new HashMap<>();
  }

  // <editor-fold defaultstate="collapsed" desc="Setter & Getter">
  public Map<String, RepositoryFile> getFiles() {
    return files;
  }

  public void setFiles(Map<String, RepositoryFile> files) {
    this.files = files;
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

  public String getRepositoryName() {
    return repositoryName;
  }

  public void setRepositoryName(String repositoryName) {
    this.repositoryName = repositoryName;
  }

  public String getRepositoryUrl() {
    return repositoryUrl;
  }

  public void setRepositoryUrl(String repositoryUrl) {
    this.repositoryUrl = repositoryUrl;
  }

  public String getLocalRepositoryPath() {
    return localRepositoryPath;
  }

  public void setLocalRepositoryPath(String localRepositoryPath) {
    this.localRepositoryPath = localRepositoryPath;
  }

  // </editor-fold>
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + Objects.hashCode(this.reference);
    hash = 23 * hash + Objects.hashCode(this.credential);
    hash = 23 * hash + Objects.hashCode(this.repositoryName);
    hash = 23 * hash + Objects.hashCode(this.repositoryUrl);
    hash = 23 * hash + Objects.hashCode(this.localRepositoryPath);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final WebhookInfo other = (WebhookInfo) obj;
    if (!Objects.equals(this.reference, other.reference)) {
      return false;
    }
    if (!Objects.equals(this.credential, other.credential)) {
      return false;
    }
    if (!Objects.equals(this.repositoryName, other.repositoryName)) {
      return false;
    }
    if (!Objects.equals(this.repositoryUrl, other.repositoryUrl)) {
      return false;
    }
    if (!Objects.equals(this.localRepositoryPath, other.localRepositoryPath)) {
      return false;
    }
    return true;
  }

}
