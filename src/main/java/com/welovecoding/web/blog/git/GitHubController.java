package com.welovecoding.web.blog.git;

import com.welovecoding.web.blog.git.exception.GitInitializationException;
import com.welovecoding.web.blog.git.exception.GitPullException;
import com.welovecoding.web.blog.github.RepositoryFile;
import com.welovecoding.web.blog.github.RepositoryFileStatus;
import com.welovecoding.web.blog.github.WebhookInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.revwalk.RevCommit;

public class GitHubController {

  private final GitHelper gh = new GitHelper();
  private static final Logger LOG = Logger.getLogger(GitHubController.class.getName());
  private WebhookInfo webhook;

  public GitHubController() {
  }

  public boolean pullFiles(WebhookInfo webhook) {
    this.webhook = webhook;

    String remotePath = webhook.getRepositoryUrl();
    String localPath = webhook.getLocalRepositoryPath();

    boolean isPulled = updateRepository(remotePath, localPath);

    return isPulled;
  }

  public void detectMovedFiles() {
    Map<String, RepositoryFile> files = webhook.getFiles();
    for (String key : files.keySet()) {
      RepositoryFile file = files.get(key);
      if (file.getStatus() == RepositoryFileStatus.ADDED) {
        String previousFilePath = parsePreviousFilePaths(file.getRelativePath());
        if (previousFilePath != null) {
          LOG.log(Level.INFO, "GOTCHA: \"{0}\" has been renamed from \"{1}\".", new Object[]{
            key,
            previousFilePath
          });
          file.setStatus(RepositoryFileStatus.MOVED);
          files.remove(previousFilePath);
        }
      }
    }
  }

  private String parsePreviousFilePaths(String relativeFilePath) {
    String previousFilePath = null;

    try {
      LogFollowCommand renamingInspector = new LogFollowCommand(gh.getRepository(), relativeFilePath);
      ArrayList<RevCommit> commits = renamingInspector.call();
      if (commits != null) {
        if (renamingInspector.getOldFilePath() != null) {
          previousFilePath = renamingInspector.getOldFilePath();
        }
      }
    } catch (IOException | GitAPIException ex) {
      // 
    }

    return previousFilePath;
  }

  private boolean updateRepository(String repositoryUrl, String localRepositoryPath) {
    boolean wasSuccessful = false;

    try {
      gh.init(repositoryUrl, localRepositoryPath);
      if (gh.pull()) {
        LOG.log(Level.INFO, "{0}: Successfully pulled Git repository.", this.getClass().getSimpleName());
        wasSuccessful = true;
      }
    } catch (GitInitializationException | GitPullException ex) {
      LOG.log(Level.SEVERE, "Error during Git pull: {0}", ex.getMessage());
    } finally {
      gh.close();
    }

    return wasSuccessful;
  }

  public void logFileStatus() {
    Map<String, RepositoryFile> files = webhook.getFiles();
    for (String key : files.keySet()) {
      RepositoryFile file = files.get(key);
      LOG.log(Level.INFO, "{0}: {1}", new Object[]{key, file.getStatus().toString()});
    }
  }

}
