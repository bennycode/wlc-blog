package com.welovecoding.web.blog.domain.article;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ArticleService {

  @EJB
  private ArticleRepository repository;

  public ArticleService() {
  }

  public void save(Article entity) {
    repository.save(entity);
  }

  public void edit(Article entity) {
    repository.edit(entity);
  }

  public Article findById(String id) {
    return repository.findById(id);
  }
}