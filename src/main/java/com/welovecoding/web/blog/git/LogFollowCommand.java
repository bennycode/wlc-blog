package com.welovecoding.web.blog.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create a Log command that enables the follow option: git log --follow -- < path
 * >
 * U
 * ser: OneWorld Example for usage: ArrayList<RevCommit> commits = new
 * LogFollowCommand(repo,"src/com/mycompany/myfile.java").call();
 */
public class LogFollowCommand {

  private final Repository repository;
  private String path;
  private Git git;
  private String oldFilePath;

  /**
   * Create a Log command that enables the follow option: git log --follow -- < path
   * >
   *
   * @param repository
   * @param path
   */
  public LogFollowCommand(Repository repository, String path) {
    this.repository = repository;
    this.path = path;
  }

  /**
   * Returns the result of a git log --follow -- < path >
   *
   * @return
   * @throws IOException
   * @throws MissingObjectException
   * @throws GitAPIException
   */
  public ArrayList<RevCommit> call() throws IOException, MissingObjectException, GitAPIException {
    ArrayList<RevCommit> commits = new ArrayList<>();
    git = new Git(repository);
    RevCommit start = null;
    do {
      Iterable<RevCommit> log = git.log().addPath(path).call();
      for (RevCommit commit : log) {
        if (commits.contains(commit)) {
          start = null;
        } else {
          start = commit;
          commits.add(commit);
        }
      }
      if (start == null) {
        return commits;
      }
    } while ((path = getRenamedPath(start)) != null);

    return commits;
  }

  /**
   * Checks for renames in history of a certain file. Returns null, if no rename
   * was found. Can take some seconds, especially if nothing is found... Here
   * might be some tweaking necessary or the LogFollowCommand must be run in a
   * thread.
   *
   * @param start
   * @return String or null
   * @throws IOException
   * @throws MissingObjectException
   * @throws GitAPIException
   */
  private String getRenamedPath(RevCommit start) throws IOException, MissingObjectException, GitAPIException {
    this.oldFilePath = null;
    Iterable<RevCommit> allCommitsLater = git.log().add(start).call();

    commitloop:
    for (RevCommit commit : allCommitsLater) {
      TreeWalk tw = new TreeWalk(repository);
      tw.addTree(commit.getTree());
      tw.addTree(start.getTree());
      tw.setRecursive(true);
      RenameDetector rd = new RenameDetector(repository);
      rd.addAll(DiffEntry.scan(tw));
      List<DiffEntry> files = rd.compute();

      diffloop:
      for (DiffEntry diffEntry : files) {
        // TODO: Break after the first FOUND and return OLD and NEW NAME
        // Then removed OLD NAME/Path from removedFiles info and make the UNTRACKED FILE
        // to a MODIFIED FILE.
        if ((diffEntry.getChangeType() == DiffEntry.ChangeType.RENAME || diffEntry.getChangeType() == DiffEntry.ChangeType.COPY) && diffEntry.getNewPath().contains(path)) {
          this.oldFilePath = diffEntry.getOldPath();
          break commitloop;
          // System.out.println("Found: " + diffEntry.toString() + " return " + diffEntry.getOldPath());
          // return diffEntry.getOldPath();
        }
      }
    }
    return null;
  }

  public String getOldFilePath() {
    return oldFilePath;
  }

  public void setOldFilePath(String oldFilePath) {
    this.oldFilePath = oldFilePath;
  }

}
