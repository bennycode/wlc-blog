package com.welovecoding.web.blog.git.exception;

public class GitException extends Exception {

  public GitException() {
  }

  public GitException(String message) {
    super(message);
  }

  public GitException(Exception ex) {
    super(ex.getMessage());
  }
}
