package com.welovecoding.web.blog.test;

import com.welovecoding.web.blog.git.Octocat;

public class RepositoryTest {

  public static void main(String[] args) throws Exception {
    Octocat cat = new Octocat();
    cat.checkoutRepository();
  }

}
