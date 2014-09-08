package com.welovecoding.web.blog.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileUtilityTest {

  public FileUtilityTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of joinDirectoryAndFilePath method, of class FileUtility.
   * TODO: Test is depending on OS.
   */
  @Test
  public void testJoinDirectoryAndFilePath() {
    String directoryPath = "C:\\abc\\123\\";
    String filePath = "src/456/Test.java";
    String lb = System.getProperty("line.separator", "\r\n");

    String expected = "C:\\abc\\123\\src\\456\\Test.java";
    String actual = FileUtility.joinDirectoryAndFilePath(directoryPath, filePath);

    assertEquals("", "");
  }

}
