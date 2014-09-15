package com.welovecoding.web.blog.git;

import com.welovecoding.web.blog.git.exception.*;
import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitHelper {

  private Git git = null;
  private Repository repository = null;

  public GitHelper() {
  }

  public void init(String remotePath, String localPath) throws GitInitializationException {
    try {
      initRepository(remotePath, localPath);
    } catch (GitException ex) {
      this.close();
      throw new GitInitializationException(ex);
    }
  }

  public void close() {
    if (repository != null) {
      repository.close();
    }

    if (git != null) {
      git.close();
    }
  }

  public boolean pull() throws GitPullException {
    boolean isSuccessful = false;

    PullCommand pull = git.pull();

    try {
      PullResult result = pull.call();
      isSuccessful = result.isSuccessful();
    } catch (GitAPIException ex) {
      throw new GitPullException(ex);
    }

    return isSuccessful;
  }

  public void updateHead(String reference) throws GitUpdateReferenceException {
    if (reference == null) {
      reference = "refs/heads/master";
    }

    try {
      RefUpdate refUpdate = repository.getRefDatabase().newUpdate(Constants.HEAD, true);
      refUpdate.setForceUpdate(true);
      refUpdate.link(reference);
    } catch (IOException ex) {
      throw new GitUpdateReferenceException(ex);
    }

  }

  public Repository getRepository() {
    return repository;
  }

  // <editor-fold desc="Repository Initialization">
  private void initRepository(String remotePath, String localPath) throws GitException {
    File directory = new File(localPath);

    if (directory.exists() && directory.isDirectory()) {
      openRepository(directory);
    } else {
      cloneRepository(remotePath, directory);
    }
  }

  private void openRepository(File directory) throws GitRepositoryReadException {
    FileRepositoryBuilder builder = new FileRepositoryBuilder()
            .readEnvironment()
            .findGitDir(directory)
            .setMustExist(true);
    try {
      this.repository = builder.build();
      this.git = new Git(repository);
    } catch (IOException ex) {
      throw new GitRepositoryReadException(ex);
    }
  }

  private void cloneRepository(String remotePath, File localRepository) throws GitCloneException {
    try {
      this.git = Git.cloneRepository().setURI(remotePath).setDirectory(localRepository).call();
      this.repository = git.getRepository();
    } catch (GitAPIException ex) {
      throw new GitCloneException(ex);
    }
  }
  // </editor-fold>

}
