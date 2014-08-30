package com.welovecoding.web.blog.git;

import com.welovecoding.web.blog.Settings;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
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
public class GitController {

  private static final Logger LOG = Logger.getLogger(GitController.class.getName());

  public void checkoutRepository() {

    File directory = getRepositoryDirectory();
    Git git = getGitRepository(directory);

    System.out.println(directory.getAbsoluteFile());

    boolean wasPulled = gitPull(git);
    System.out.println("wasPulled: " + wasPulled);

    git.getRepository().close();
    git.close();

  }

  private Git getGitRepository(File directory) {
    Git git;

    if (directory.exists() && directory.isDirectory()) {
      LOG.log(Level.INFO, "Git repository already exists: {0}", directory.getAbsolutePath());
      Repository repository = openRepository(directory);
      git = new Git(repository);
    } else {
      LOG.log(Level.INFO, "Repository does not exist. We will clone it to: {0}", directory.getAbsolutePath());
      git = cloneRepository(directory);
    }

    return git;
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
        LOG.log(Level.INFO, "Commit: https://github.com/bennyn/wlc-blog/commit/{0}", revision.toObjectId().getName());
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
