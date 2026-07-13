    package kr.co.sscm.common.util;

import java.security.MessageDigest;
import org.springframework.security.crypto.password.PasswordEncoder;
import sun.misc.BASE64Encoder;

public class LegacyPasswordEncoder implements PasswordEncoder {
   public String encode(CharSequence arg0) {
      try {
         return this.encodeSha(arg0.toString());
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public boolean matches(CharSequence arg0, String arg1) {
      try {
         return this.encodeSha(arg0.toString()).equals(arg1);
      } catch (Exception var4) {
         var4.printStackTrace();
         return false;
      }
   }

   public String encodeSha(String word) throws Exception {
      try {
         MessageDigest md = MessageDigest.getInstance("SHA");
         md.update(word.getBytes());
         byte[] raw = md.digest();
         BASE64Encoder encoder = new BASE64Encoder();
         return encoder.encode(raw);
      } catch (Exception var5) {
         throw new IllegalStateException(var5);
      }
   }
}
