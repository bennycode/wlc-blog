package com.welovecoding.web.blog.domain.article;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Article implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String id;

  @Temporal(TemporalType.TIMESTAMP)
  private Calendar published;

  private String title;
  private String description;
  private String html;
  private List<String> tags;

  public Article() {
    this.published = new GregorianCalendar();
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Calendar getPublished() {
    return published;
  }

  public void setPublished(Calendar published) {
    this.published = published;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Article)) {
      return false;
    }
    Article other = (Article) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    String lb = System.getProperty("line.separator", "\r\n");

    String template = "Entity: {0}"
            + lb
            + "Title: {1}"
            + lb
            + "Description: {2}"
            + lb
            + "Tags: {3}"
            + lb
            + "HTML: {4}";

    Object[] values = new Object[]{
      this.getClass().getName() + "[ id=" + id + " ]",
      this.title,
      this.description,
      Arrays.toString(this.tags.toArray()),
      this.html
    };

    return MessageFormat.format(template, values);
  }

}
