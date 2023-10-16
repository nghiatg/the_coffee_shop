package com.coffeeshopauthentication.client;


import static org.springframework.http.HttpMethod.POST;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.coffeeshopauthentication.config.KeycloakConfig;
import com.coffeeshopauthentication.dto.UserType;
import com.coffeeshopauthentication.dto.request.KeycloakCreateUserRequest;
import com.coffeeshopauthentication.dto.request.RegisterRequest;
import com.coffeeshopauthentication.dto.response.KeycloakClientLoginResponse;
import com.coffeeshopauthentication.dto.response.UserLoginResponse;

@Component
public class KeycloakClient {

    private static final String URL_GET_USER_TOKEN_PATTERN = "#KEYCLOAK_HOST/realms/#KEYCLOAK_REALM/protocol/openid-connect/token";

    private static final String URL_GET_CLIENT_TOKEN_PATTERN = "#KEYCLOAK_HOST/realms/#KEYCLOAK_REALM/protocol/openid-connect/token";

    private static final String URL_CREATE_USER_PATTERN = "#KEYCLOAK_HOST/admin/realms/#KEYCLOAK_REALM/users";

    @Autowired
    private KeycloakConfig keycloakConfig;

    @Autowired
    private RestTemplate restTemplate;

    public UserLoginResponse login(String username, String password) {
        String urlGetToken = URL_GET_USER_TOKEN_PATTERN.replace("#KEYCLOAK_HOST", keycloakConfig.getKeycloakHost())
                                                    .replace("#KEYCLOAK_REALM", keycloakConfig.getKeycloakRealm());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");
        map.add("client_id", keycloakConfig.getKeycloakClientId());
        map.add("client_secret", keycloakConfig.getKeycloakClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<UserLoginResponse> loginResponseEntity = restTemplate.exchange(urlGetToken, POST, requestEntity, UserLoginResponse.class);
        
        return loginResponseEntity.getBody();
    }

    private KeycloakClientLoginResponse clientLogin() {
        String urlGetToken = URL_GET_CLIENT_TOKEN_PATTERN.replace("#KEYCLOAK_HOST", keycloakConfig.getKeycloakHost())
                                                    .replace("#KEYCLOAK_REALM", keycloakConfig.getKeycloakRealm());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", keycloakConfig.getKeycloakClientId());
        map.add("client_secret", keycloakConfig.getKeycloakClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<KeycloakClientLoginResponse> loginResponseEntity = 
                restTemplate.exchange(urlGetToken, POST, requestEntity, KeycloakClientLoginResponse.class);
        
        return loginResponseEntity.getBody();
    }

    public void createUser(RegisterRequest registerRequest) {
        String clientAccessToken = clientLogin().getAccessToken();
        String urlGetToken = URL_CREATE_USER_PATTERN.replace("#KEYCLOAK_HOST", keycloakConfig.getKeycloakHost())
                                                    .replace("#KEYCLOAK_REALM", keycloakConfig.getKeycloakRealm());
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + clientAccessToken);

        KeycloakCreateUserRequest createUserReq = 
            KeycloakCreateUserRequest.builder()
            .username(registerRequest.getUsername())
            .enabled("true")
            .groups(registerRequest.getUserTypes().stream().map(UserType::getKeyCloakGroupName).collect(Collectors.toList()))
            .credentials(
                List.of(KeycloakCreateUserRequest.Credentials.builder().type("password").value(registerRequest.getPassword()).temporary(false).build())
            )
            .build();

        HttpEntity<KeycloakCreateUserRequest> requestEntity = new HttpEntity<>(createUserReq, headers);

        restTemplate.exchange(urlGetToken, POST, requestEntity, String.class);
    }

    
}
