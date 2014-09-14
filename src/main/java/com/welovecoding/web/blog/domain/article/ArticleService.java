package com.welovecoding.web.blog.domain.article;

import java.util.List;
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

  public Article getById(String id) {
    return repository.getById(id);
  }

  public Article getByLanguageCodeAndSlug(String languageCode, String slug) {
    return repository.getByLanguageCodeAndSlug(languageCode, slug);
  }

  public List<Article> getAll() {
    return repository.getAll();
  }
}
