package com.welovecoding.web.blog;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.MergeResult.MergeStatus;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * https://github.com/centic9/jgit-cookbook
 *
 * @author Benny
 */
public class Octocat {

  private static final Logger LOG = Logger.getLogger(Octocat.class.getName());

  public void checkoutRepository() throws IOException {
    File directory = getRepositoryDirectory();
//    Repository repository = null;
    Git git = null;

    if (directory.exists() && directory.isDirectory()) {
      LOG.log(Level.INFO, "Git repository already exists: {0}", directory.getAbsolutePath());
      Repository repository = openRepository(directory);
      git = new Git(repository);
    } else {
      git = cloneRepository(directory);
    }

    boolean hasBeenPulled = gitPull(git);
    System.out.println("hasBeenPulled: " + hasBeenPulled);

    git.getRepository().close();
    git.close();

  }

  private boolean gitPull(Git git) {
    boolean isSuccessful = false;

    PullCommand pull = git.pull();
    PullResult result;
    
    try {
      result = pull.call();
      MergeResult mergeResult = result.getMergeResult();
      MergeStatus mergeStatus = mergeResult.getMergeStatus();
      isSuccessful = mergeStatus.isSuccessful();
    } catch (GitAPIException ex) {
      LOG.log(Level.SEVERE, "Error during ''git pull'' - {0}", ex.getMessage());
    }

    return isSuccessful;
  }

  private Repository openRepository(File directory) throws IOException {
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    Repository repository = builder.setGitDir(directory).readEnvironment().findGitDir().build();
    return repository;
  }

  private Git cloneRepository(File directory) {
    Git call = null;

    try {
      call = Git.cloneRepository().setURI(Settings.GITHUB_REPOSITORY).setDirectory(directory).call();
      // repository.close();
    } catch (GitAPIException ex) {
      LOG.log(Level.SEVERE, "There were problems when executing Git: {0}", ex.getMessage());
    }

    return call;

  }

  private File getRepositoryDirectory() {
    String home = System.getProperty("user.home");
    String name = parseRepositoryName(Settings.GITHUB_REPOSITORY);
    File checkoutPath = new File(home + System.getProperty("file.separator") + name);
    return checkoutPath;
  }

  private String parseRepositoryName(String name) {
    int beginIndex = name.lastIndexOf("/");
    return name.substring(beginIndex);
  }
}
