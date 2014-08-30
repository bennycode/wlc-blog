package com.welovecoding.web.blog;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.MergeResult.MergeStatus;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.FetchResult;

/**
 * https://github.com/centic9/jgit-cookbook
 *
 * @author Benny
 */
public class Octocat {

  private static final Logger LOG = Logger.getLogger(Octocat.class.getName());

  public void test() {
    File directory = getRepositoryDirectory();
    Repository repository = openRepository(directory);
    Git git = new Git(repository);

    gitPull(git);
  }

  public void checkoutRepository() throws IOException {
    File directory = getRepositoryDirectory();
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

  private void gitFetch(Git git) {

    FetchCommand fetch = git.fetch();

    try {
      FetchResult result = fetch.call();
    } catch (GitAPIException ex) {
      LOG.log(Level.SEVERE, "Error during \"git fetch\": {0}", ex.getMessage());
    }

  }

  private boolean gitPull(Git git) {
    boolean isSuccessful = false;

    PullCommand pull = git.pull();

    try {
      // Pull request
      PullResult result = pull.call();
      isSuccessful = result.isSuccessful();
    } catch (GitAPIException ex) {
      LOG.log(Level.SEVERE, "Error during \"git pull\": {0}", ex.getMessage());
    }

    return isSuccessful;
  }

  private void updateHead(Git git, String reference) {
    Repository repository = git.getRepository();

    if (reference == null) {
      reference = "refs/heads/master";
    }

    try {
      RefUpdate refUpdate = repository.getRefDatabase().newUpdate(Constants.HEAD, true);
      refUpdate.setForceUpdate(true);
      refUpdate.link(reference);
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Error during HEAD update: {0}", ex.getMessage());
    }

  }

  private Repository openRepository(File directory) {
    FileRepositoryBuilder builder = new FileRepositoryBuilder()
            .readEnvironment()
            .findGitDir(directory)
            .setMustExist(true);

    Repository repository = null;

    try {
      repository = builder.build();
      ObjectId revision = repository.resolve(Constants.HEAD);
      if (revision != null) {
        LOG.log(Level.INFO, "Commit: {0}", revision);
      } else {
        // TODO: Throw error
        LOG.log(Level.SEVERE, "HEAD cannot be found. Maybe we should update the HEAD to 'refs/heads/master'.");
      }
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Repository is not a valid Git repository: {0}", ex.getMessage());
    }

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
