package com.coffeeshopauthentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.coffeeshopauthentication.util.TokenBlacklistUtils;
import static com.coffeeshopauthentication.util.TokenBlacklistUtils.extractTokenFromBearerHeader;
import static com.coffeeshopauthentication.config.Constants.AUTHORIZATION_HEADER;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomLogoutHandler implements LogoutHandler {

   @Autowired
   private TokenBlacklistUtils tokenBlacklistUtils;

   @Override
   public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
      String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
      tokenBlacklistUtils.addTokenToBlackList(extractTokenFromBearerHeader(bearerToken));
   }

   
}
