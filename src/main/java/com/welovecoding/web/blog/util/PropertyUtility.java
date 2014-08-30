package com.welovecoding.web.blog.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class PropertyUtility {

  public static String getEnvironmentEntry(String key) {
    String value;

    try {
      Context context = new InitialContext();
      Context environment = (Context) context.lookup("java:comp/env");
      value = (String) environment.lookup(key);
    } catch (NamingException ex) {
      value = "";
    }

    return value;
  }

}
