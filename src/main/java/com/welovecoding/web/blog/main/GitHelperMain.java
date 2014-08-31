package com.welovecoding.web.blog.main;

import com.welovecoding.web.blog.git.GitHelper;
import com.welovecoding.web.blog.git.exception.GitInitializationException;
import com.welovecoding.web.blog.git.exception.GitPullException;

public class GitHelperMain {

  public static void main(String[] args) {
    String repositoryUrl = "https://github.com/bennyn/wlc-blog";
    String localRepositoryPath = "C:\\Users\\Benny\\wlc-blog";

    GitHelper gh = new GitHelper();

    try {
      gh.init(repositoryUrl, localRepositoryPath);
      boolean wasPulled = gh.pull();
      System.out.println("Pulled? " + wasPulled);
    } catch (GitInitializationException | GitPullException ex) {
      System.out.println("Error: " + ex.getMessage());
    }
  }

}
