package com.welovecoding.web.blog.domain.article;

import static com.welovecoding.web.blog.settings.Settings.PERSISTENCE_UNIT_NAME;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

@Stateless
public class ArticleRepository {

  @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
  EntityManager em;

  public ArticleRepository() {
  }

  public void save(Article entity) {
    em.persist(entity);
  }

  public void edit(Article entity) {
    em.merge(entity);
  }

  public Article getById(String id) {
    Article entity = null;

    TypedQuery<Article> query = em.createNamedQuery(Article.FIND_BY_ID, Article.class);
    query.setParameter("id", id);

    try {
      entity = query.getSingleResult();
    } catch (NoResultException e) {
      //
    }

    return entity;
  }

  public List<Article> getAll() {
    CriteriaQuery<Article> criteria = em.getCriteriaBuilder().createQuery(Article.class);
    criteria.select(criteria.from(Article.class));
    TypedQuery<Article> query = em.createQuery(criteria);
    return query.getResultList();
  }

  public Article getByLanguageCodeAndSlug(String languageCode, String slug) {
    Article entity = null;

    TypedQuery<Article> query = em.createNamedQuery(Article.FIND_BY_LANGUAGE_AND_SLUG, Article.class);
    query.setParameter("languageCode", languageCode);
    query.setParameter("slug", slug);

    try {
      entity = query.getSingleResult();
    } catch (NoResultException e) {
      //
    }

    return entity;
  }

}
