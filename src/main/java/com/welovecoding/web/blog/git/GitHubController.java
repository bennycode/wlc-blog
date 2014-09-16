package com.welovecoding.web.blog.git;

import com.welovecoding.web.blog.git.exception.GitInitializationException;
import com.welovecoding.web.blog.git.exception.GitPullException;
import com.welovecoding.web.blog.github.WebhookInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

public class GitHubController {

  private final GitHelper gh = new GitHelper();
  private static final Logger LOG = Logger.getLogger(GitHubController.class.getName());

  public GitHubController() {
  }

  public boolean pullFiles(WebhookInfo webhook) {
    String remotePath = webhook.getRepositoryUrl();
    String localPath = webhook.getLocalRepositoryPath();

    boolean isPulled = updateRepository(remotePath, localPath);

    try {
      // Check for renamed files
      // TODO: New & Removed file can be a hint for a renamed file! We have to check that!
      for (String newFile : webhook.getUntrackedFiles()) {
        LogFollowCommand renamingInspector = new LogFollowCommand(gh.getRepository(), newFile);
        ArrayList<RevCommit> commits = renamingInspector.call();
        if (commits != null) {
          if (renamingInspector.getOldFilePath() != null) {
            LOG.log(Level.INFO, "\"{0}\" has been renamed from \"{1}\".", new Object[]{
              newFile,
              renamingInspector.getOldFilePath()
            });
          } else {
            LOG.log(Level.INFO, "New file: " + newFile);
          }
        }
      }
    } catch (IOException | GitAPIException ex) {
      Logger.getLogger(GitHubController.class.getName()).log(Level.SEVERE, null, ex);
    }

    return isPulled;
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

}
