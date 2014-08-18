package com.welovecoding.web.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class GitHubUtility {

  public static String hash_hmac(String baseString, String keyString) {
    String hash = "";

    try {
      SecretKey secretKey = null;

      byte[] keyBytes = keyString.getBytes();
      secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

      Mac mac = Mac.getInstance("HmacSHA1");

      mac.init(secretKey);

      byte[] text = baseString.getBytes();
      hash = new String(Base64.getEncoder().encode(mac.doFinal(text))).trim();
    } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
//
    }

    return hash;
  }
}
