package com.welovecoding.web.blog.author;

import static com.welovecoding.web.blog.author.Author.FIND_ALL;
import static com.welovecoding.web.blog.author.Author.ORDER_BY_NAME;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({
  @NamedQuery(name = FIND_ALL, query = "SELECT a FROM Author a"),
  @NamedQuery(name = ORDER_BY_NAME, query = "SELECT a FROM Author a ORDER BY a.name")
})
public class Author implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String FIND_ALL = "Author.findAll";
  public static final String ORDER_BY_NAME = "Author.orderByName";

  @Id
  private Long id;

  @Size(min = 0, max = 255)
  private String name;

  @Temporal(TemporalType.TIMESTAMP)
  private Calendar registered;

  public Author() {
  }

  public Author(String name) {
    this.name = name;
  }

  public Author(String name, Calendar registered) {
    this.name = name;
    this.registered = registered;
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Calendar getRegistered() {
    return registered;
  }

  public void setRegistered(Calendar registered) {
    this.registered = registered;
  }

}
