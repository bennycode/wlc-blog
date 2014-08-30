package com.welovecoding.web.blog.test;

import com.welovecoding.web.blog.git.GitController;

public class RepositoryTest {

  public static void main(String[] args) throws Exception {
    GitController cat = new GitController();
    cat.checkoutRepository();
  }

}
