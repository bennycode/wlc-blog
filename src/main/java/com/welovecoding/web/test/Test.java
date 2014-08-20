package com.welovecoding.web.test;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class Test {

  private static final String REMOTE_URL = "https://github.com/bennyn/wlc-blog";

  public static void main(String[] args) throws Exception {
    String homeDirectory = detectHomeDirectory();
    String repositoryName = parseRepositoryName(REMOTE_URL);
    File checkoutPath = createRepositoryCheckoutPath(homeDirectory, repositoryName);

    // then clone
    System.out.println("Cloning from " + REMOTE_URL + " to " + checkoutPath);
    Git.cloneRepository()
            .setURI(REMOTE_URL)
            .setDirectory(checkoutPath)
            .call();

    // now open the created repository
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    Repository repository = builder.setGitDir(checkoutPath)
            .readEnvironment() // scan environment GIT_* variables
            .findGitDir() // scan up the file system tree
            .build();

    System.out.println("Having repository: " + repository.getDirectory());

    repository.close();
  }

  private static String detectHomeDirectory() {
    return System.getProperty("user.home");
  }

  private static String parseRepositoryName(String name) {
    int beginIndex = name.lastIndexOf("/");
    return name.substring(beginIndex);
  }

  /**
   * ...
   *
   * @param home Path of the root directory for the checkout (Example:
   * C:\Users\Benny)
   * @param name Name of the Repository (Example: wlc-blog)
   * @return Representation of the checkout path (Example:
   * C:\Users\Benny\wlc-blog)
   */
  private static File createRepositoryCheckoutPath(String home, String name) {
    return new File(home + System.getProperty("file.separator") + name);
  }

}
