package com.gabriel.oauth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping("/")
  public OAuth2AccessToken acessToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
    return authorizedClient.getAccessToken();
  }

  @GetMapping("/user")
  public OAuth2User index(@AuthenticationPrincipal OAuth2User oAuth2User) {
    return oAuth2User;
  }

  @GetMapping("/teste")
  public String teste() {
    return "Rota de teste";
  }

  /*
   * OidcUser (OpenID Connect) é uma interface que representa o usuário que está registrado com OpenID.
   * Ele fornece informações do perfil do usuário (nome, e-mail, token JWT, etc).
  */
  @GetMapping("/oidc")
  public OidcUser user(@AuthenticationPrincipal OidcUser principal) {
    return principal;
  }
}
