package com.welovecoding.web.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class GitHubUtility {

  private static final Logger LOG = Logger.getLogger(GitHubUtility.class.getName());
  private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
  private static final char[] HEX = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  public static boolean verifySignature(String payload, String signature, String secret) {
    boolean isValid = false;

    try {

      Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
      SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
      mac.init(signingKey);
      byte[] rawHmac = mac.doFinal(payload.getBytes());

      String expected = signature.substring(5);
      String actual = new String(encode(rawHmac));

      isValid = expected.equals(actual);

    } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException ex) {

      LOG.log(Level.WARNING, ex.getLocalizedMessage());

    }

    return isValid;
  }

  private static char[] encode(byte[] bytes) {
    final int amount = bytes.length;
    char[] result = new char[2 * amount];

    int j = 0;
    for (int i = 0; i < amount; i++) {
      result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
      result[j++] = HEX[(0x0F & bytes[i])];
    }

    return result;
  }

}
