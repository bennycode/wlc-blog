package com.welovecoding.web.blog.domain.article;

import static com.welovecoding.web.blog.settings.Settings.PERSISTENCE_UNIT_NAME;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ArticleRepository {

  @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
  EntityManager em;

  public ArticleRepository() {
  }

  public void save(Article entity) {
    em.persist(entity);
  }
}
