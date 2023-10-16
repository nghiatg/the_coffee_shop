package com.cofeeshop.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;

@Component
public class UserUtils {
    
    public String retrieveCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwtPrincipal = (Jwt)(auth.getPrincipal());
        return Optional.ofNullable(jwtPrincipal.getClaims())
                        .map(claimSet -> claimSet.get("preferred_username"))
                        .map(String::valueOf)
                        .orElse("");
    }

}
