package com.cofeeshop.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;

@Component
public class RolesClaimConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter wrappedConverter;

    public RolesClaimConverter() {
        wrappedConverter = new JwtGrantedAuthoritiesConverter();
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        var grantedAuthorities = new ArrayList<>(wrappedConverter.convert(jwt));
        var roles = (List<String>)(((LinkedTreeMap)jwt.getClaims().get("realm_access")).get("roles"));
        if (roles != null) {
            for (String role : roles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
        }

        return new JwtAuthenticationToken(jwt, grantedAuthorities);
    }
}
