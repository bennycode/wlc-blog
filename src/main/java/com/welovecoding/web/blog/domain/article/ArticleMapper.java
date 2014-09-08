package com.welovecoding.web.blog.domain.article;

import com.welovecoding.web.blog.markdown.meta.BufferedMarkdownMetaParser;
import com.welovecoding.web.blog.markdown.meta.MarkdownMetaData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.markdown4j.Markdown4jProcessor;

public class ArticleMapper {

  private static final Logger LOG = Logger.getLogger(ArticleMapper.class.getName());
  private Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();

  public void mapArticleFromMarkdownFile(String absolutePath) {

    if (absolutePath.endsWith(".md")) {
      File markdownFile = new File(absolutePath);
      parseMarkdownFile(markdownFile);
    }
  }

  private void parseMarkdownFile(File markdownFile) {
    BufferedMarkdownMetaParser metaParser = new BufferedMarkdownMetaParser();
    Map<String, MarkdownMetaData> metaData;

    StringBuilder content = new StringBuilder();
    String line;

    try (
            FileInputStream fis = new FileInputStream(markdownFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");) {

      BufferedReader reader = new BufferedReader(isr);

      while ((line = reader.readLine()) != null) {
        // markdown4jProcessor.process(line);
        metaParser.parse(line);
      }

      metaData = metaParser.getData();
      metaParser.print();

    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
      LOG.log(Level.SEVERE, "Error while opening the file: {0}", ex.getMessage());
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Error while reading the file: {0}", ex.getMessage());
    }

  }

}
