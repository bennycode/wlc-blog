package com.welovecoding.web.blog.domain.article;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ArticleController implements Serializable {

  @EJB
  private ArticleService articleService;

  public Article getGermanArticleBySlug(String slug) {
    String languageCode = Locale.GERMAN.getLanguage();
    return articleService.getByLanguageCodeAndSlug(languageCode, slug);
  }

  public List<Article> getArticles() {
    return articleService.getAll();
  }

}
