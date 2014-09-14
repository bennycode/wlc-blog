package com.welovecoding.web.blog.markdown;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.markdown4j.Markdown4jProcessor;

/**
 * http://alexgorbatchev.com/SyntaxHighlighter/
 *
 * @author Benny
 */
public class MarkdownParser {

  private final Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();
  private boolean isCode = false;
  private final String CODE_PATTERN = "```";

  public MarkdownParser() {
  }

  public String process(String input) {
    String programmingLanguage = "bash";

    // End of code block
    if (isCode && input.startsWith(CODE_PATTERN)) {
      isCode = false;
      System.out.println("CODE END");
      return "</pre>";
    }

    if (isCode) {
      System.out.println("CODE: " + input);
      return input;
    }

    // Begin of code block
    if (input.startsWith(CODE_PATTERN)) {
      isCode = true;
      System.out.println("CODE START!");
      String test = input.substring(input.indexOf(CODE_PATTERN) + 1);

      System.out.println("LANG: " + test);

      return "<pre class=\"brush: " + programmingLanguage + ";\">";
    }

    // Usual markdown
    String rendered = "";

    try {
      rendered = markdown4jProcessor.process(input);
    } catch (IOException ex) {
      Logger.getLogger(MarkdownParser.class.getName()).log(Level.SEVERE, null, ex);
    }

    return rendered;
  }
}
