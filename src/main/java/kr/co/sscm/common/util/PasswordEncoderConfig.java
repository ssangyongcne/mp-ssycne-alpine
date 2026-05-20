    package kr.co.sscm.common.util;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
   @Bean
   public PasswordEncoder passwordEncoder() {
      String bcrypt = "bcrypt";
      Map<String, PasswordEncoder> encoders = new HashMap();
      encoders.put(bcrypt, new BCryptPasswordEncoder(10));
      LegacyPasswordEncoder legacy = new LegacyPasswordEncoder();
      encoders.put("legacy", legacy);
      DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(bcrypt, encoders);
      passwordEncoder.setDefaultPasswordEncoderForMatches(legacy);
      return passwordEncoder;
   }
}