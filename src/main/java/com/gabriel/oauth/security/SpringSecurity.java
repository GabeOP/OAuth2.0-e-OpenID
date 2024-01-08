package com.gabriel.oauth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults())
            .oauth2ResourceServer(Customizer.withDefaults())
            .build();
  }
}
