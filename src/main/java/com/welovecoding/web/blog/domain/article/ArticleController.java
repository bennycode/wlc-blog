package com.welovecoding.web.blog.domain.article;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ArticleController implements Serializable {

  @EJB
  private ArticleService articleService;

  public List<Article> getArticles() {
    return articleService.getAll();
  }

}
