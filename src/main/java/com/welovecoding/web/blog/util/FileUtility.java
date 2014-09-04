package com.welovecoding.web.blog.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

public class FileUtility {

  private static final Logger LOG = Logger.getLogger(FileUtility.class.getName());

  public static String readFileContent(Path path) {
    File file = path.toAbsolutePath().toFile();
    return readFileContent(file);
  }

  public static String readFileContent(File file) {
    String result = "";

    try {
      InputStream is = new FileInputStream(file);
      BufferedInputStream bis = new BufferedInputStream(is);
      result = IOUtils.toString(bis, "UTF-8");
    } catch (IOException ex) {
      LOG.log(Level.WARNING, ex.getMessage());
    }

    return result;
  }
}
