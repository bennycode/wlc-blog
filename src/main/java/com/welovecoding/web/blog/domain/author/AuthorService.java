package com.welovecoding.web.blog.author;

import java.util.GregorianCalendar;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class AuthorService {

  @EJB
  private AuthorRepository repository;

  public void saveTestAuthor() {
    Author author = new Author("Benny", new GregorianCalendar());
    repository.save(author);
  }

}
