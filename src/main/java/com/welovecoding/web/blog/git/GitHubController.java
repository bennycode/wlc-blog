package com.welovecoding.web.blog.git;

import com.welovecoding.web.blog.git.exception.GitInitializationException;
import com.welovecoding.web.blog.git.exception.GitPullException;
import com.welovecoding.web.blog.github.WebhookInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitHubController {

  private static final Logger LOG = Logger.getLogger(GitHubController.class.getName());
  private WebhookInfo webhook;

  public GitHubController(WebhookInfo webhook) {
    this.webhook = webhook;
  }

  public void process() {
    String remotePath = webhook.getRepositoryUrl();
    String localPath = webhook.getLocalRepositoryPath();

    updateRepository(remotePath, localPath);
  }

  private void updateRepository(String repositoryUrl, String localRepositoryPath) {
    GitHelper gh = new GitHelper();

    try {
      gh.init(repositoryUrl, localRepositoryPath);
      if (gh.pull()) {
        LOG.log(Level.INFO, "Successfully updated Git repository.");
      }
    } catch (GitInitializationException | GitPullException ex) {
      LOG.log(Level.SEVERE, "Error during Git pull: {0}", ex.getMessage());
    } finally {
      gh.close();
    }
  }

}
