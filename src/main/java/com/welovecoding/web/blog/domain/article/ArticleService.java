package com.welovecoding.web.blog.domain.article;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaQuery;

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

  public Article getById(String id) {
    return repository.getById(id);
  }

  public List<Article> getAll() {
    return repository.getAll();
  }
}
