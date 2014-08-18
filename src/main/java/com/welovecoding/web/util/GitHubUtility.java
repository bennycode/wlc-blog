package com.welovecoding.web.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class GitHubUtility {

  private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
  private static final char[] HEX = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  public static boolean verifySignature(String payload, String signature, String secret) {
    if (!signature.startsWith("sha1=")) {
      return false;
    }

    boolean isValid = false;

    try {
      String expected = signature.substring(5);
      Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
      SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
      mac.init(signingKey);
      byte[] rawHmac = mac.doFinal(payload.getBytes());
      String actual = new String(encode(rawHmac));
      isValid = expected.equals(actual);
    } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException ex) {
      // 
    }

    return isValid;
  }

  public static char[] encode(byte[] bytes) {
    final int nBytes = bytes.length;
    char[] result = new char[2 * nBytes];

    int j = 0;
    for (int i = 0; i < nBytes; i++) {
      result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
      result[j++] = HEX[(0x0F & bytes[i])];
    }

    return result;
  }

}
