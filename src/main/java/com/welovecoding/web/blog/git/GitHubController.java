package com.welovecoding.web.blog.git;

import com.welovecoding.web.blog.git.exception.GitInitializationException;
import com.welovecoding.web.blog.git.exception.GitPullException;
import com.welovecoding.web.blog.github.WebhookInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitHubController {

  private static final Logger LOG = Logger.getLogger(GitHubController.class.getName());
  public WebhookInfo webhook;

  public GitHubController(WebhookInfo webhook) {
    this.webhook = webhook;
  }

  public boolean process() {
    String remotePath = webhook.getRepositoryUrl();
    String localPath = webhook.getLocalRepositoryPath();

    return updateRepository(remotePath, localPath);
  }

  private boolean updateRepository(String repositoryUrl, String localRepositoryPath) {
    GitHelper gh = new GitHelper();
    boolean wasSuccessful = false;

    try {
      gh.init(repositoryUrl, localRepositoryPath);
      if (gh.pull()) {
        LOG.log(Level.INFO, "Successfully updated Git repository.");
        wasSuccessful = true;
      }
    } catch (GitInitializationException | GitPullException ex) {
      LOG.log(Level.SEVERE, "Error during Git pull: {0}", ex.getMessage());
    } finally {
      gh.close();
      return wasSuccessful;
    }
  }

}
