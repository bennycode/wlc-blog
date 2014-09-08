package com.welovecoding.web.blog.domain.article;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleMapper {

  private static final Logger LOG = Logger.getLogger(ArticleMapper.class.getName());

  public void mapArticleFromMarkdownFile(String absolutePath) {
    if (absolutePath.endsWith(".md")) {
      File markdownFile = new File(absolutePath);
      parseMarkdownFile(markdownFile);
    }
  }

  private void parseMarkdownFile(File markdownFile) {
    StringBuilder sb = new StringBuilder();
    String line;

    try (
            FileInputStream fis = new FileInputStream(markdownFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");) {
      BufferedReader reader = new BufferedReader(isr);
      while ((line = reader.readLine()) != null) {
        System.out.println("JA: " + line);
      }
    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
      LOG.log(Level.SEVERE, "Error while opening the file: {0}", ex.getMessage());
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Error while reading the file: {0}", ex.getMessage());
    }

  }

}
