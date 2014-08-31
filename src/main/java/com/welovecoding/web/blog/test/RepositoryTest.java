package com.welovecoding.web.blog.test;

import com.welovecoding.web.blog.util.GitUtility;

public class RepositoryTest {

  public static void main(String[] args) throws Exception {
    GitUtility cat = new GitUtility();
    cat.checkoutRepository();
  }

}
