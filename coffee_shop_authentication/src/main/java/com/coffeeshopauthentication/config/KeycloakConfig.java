package com.coffeeshopauthentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class KeycloakConfig {

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String keycloakClientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String keycloakClientSecret;

    @Value("${spring.security.oauth2.client.registration.keycloak.realm}")
    private String keycloakRealm;

    @Value("${spring.security.oauth2.client.registration.keycloak.host}")
    private String keycloakHost;

}
