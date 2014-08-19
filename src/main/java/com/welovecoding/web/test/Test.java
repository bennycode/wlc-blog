package com.welovecoding.web.test;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class Test {

  private static final String REMOTE_URL = "https://github.com/bennyn/wlc-blog";

  public static void main(String[] args) throws Exception {
    File localPath = File.createTempFile("TestGitRepository", "");
    localPath.delete();

    // then clone
    System.out.println("Cloning from " + REMOTE_URL + " to " + localPath);
    Git.cloneRepository()
            .setURI(REMOTE_URL)
            .setDirectory(localPath)
            .call();

    // now open the created repository
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    Repository repository = builder.setGitDir(localPath)
            .readEnvironment() // scan environment GIT_* variables
            .findGitDir() // scan up the file system tree
            .build();

    System.out.println("Having repository: " + repository.getDirectory());

    repository.close();
  }

}
