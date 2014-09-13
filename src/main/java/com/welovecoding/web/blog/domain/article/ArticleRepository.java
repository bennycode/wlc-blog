package com.welovecoding.web.blog.domain.article;

import static com.welovecoding.web.blog.settings.Settings.PERSISTENCE_UNIT_NAME;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

@Stateless
public class ArticleRepository {

  @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
  EntityManager em;

  public ArticleRepository() {
  }

  public List<Article> getAll() {
    CriteriaQuery<Article> cq = em.getCriteriaBuilder().createQuery(Article.class);
    cq.select(cq.from(Article.class));
    return em.createQuery(cq).getResultList();
  }

  public Article getById(String id) {
    Article entity = null;

    try {
      entity = em.createNamedQuery(Article.FIND_BY_ID, Article.class).setParameter("id", id).getSingleResult();
    } catch (NoResultException e) {
      //
    }

    return entity;
  }

  public void save(Article entity) {
    em.persist(entity);
  }

  public void edit(Article entity) {
    em.merge(entity);
  }
}
