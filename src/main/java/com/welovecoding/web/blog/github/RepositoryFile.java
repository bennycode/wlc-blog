package com.welovecoding.web.blog.github;

public class RepositoryFile {

  private String absoluteLocalPath;
  private String relativePath;
  private String oldRelativePath;
  private RepositoryFileStatus status;

  public RepositoryFile() {
  }

  public String getRelativePath() {
    return relativePath;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  public String getOldRelativePath() {
    return oldRelativePath;
  }

  public void setOldRelativePath(String oldRelativePath) {
    this.oldRelativePath = oldRelativePath;
  }

  public RepositoryFileStatus getStatus() {
    return status;
  }

  public void setStatus(RepositoryFileStatus status) {
    this.status = status;
  }

  public String getAbsoluteLocalPath() {
    return absoluteLocalPath;
  }

  public void setAbsoluteLocalPath(String absoluteLocalPath) {
    this.absoluteLocalPath = absoluteLocalPath;
  }
  
}
