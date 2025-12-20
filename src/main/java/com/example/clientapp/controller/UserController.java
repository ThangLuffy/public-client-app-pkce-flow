package com.example.clientapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
//        TODO integrate with view
//        TODO define config webclient, default set access token for header
//        TODO define page view to display response from resource server
//        TODO customize view of home page, login page
    @GetMapping("/api/users/unit-test")
    public String findUsers(@RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient client) {
        log.info("Token from OAuth2AuthorizedClient: '{}'",
                client != null && client.getAccessToken() != null ? client.getAccessToken().getTokenValue() : null);
        String accessToken = client != null && client.getAccessToken() != null ? client.getAccessToken().getTokenValue() : null;
        if (ObjectUtils.isEmpty(accessToken)) {
            return "access token is empty in client application";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:8081/api/users",
                        HttpMethod.GET,
                        entity,
                        String.class
                );

        return response.getBody();
    }
}
