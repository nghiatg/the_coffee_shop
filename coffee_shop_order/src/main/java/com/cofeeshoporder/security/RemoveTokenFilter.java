package com.cofeeshoporder.security;

import static com.cofeeshoporder.config.Constants.AUTHORIZATION_HEADER;
import static com.cofeeshoporder.util.TokenBlacklistUtils.extractTokenFromBearerHeader;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cofeeshoporder.util.TokenBlacklistUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@Component
public class RemoveTokenFilter implements Filter {
    
    @Autowired
    private TokenBlacklistUtils tokenBlacklistUtils;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String authorizationHeader = httpRequest.getHeader(AUTHORIZATION_HEADER);

        boolean tokenIsBlacklisted = 
            Optional.ofNullable(authorizationHeader)
                    .map(TokenBlacklistUtils::extractTokenFromBearerHeader)
                    .map(tokenBlacklistUtils::verifyTokenInBlackList)
                    .orElse(false);
        if(tokenIsBlacklisted) {
            CustomHttpServletRequestWrapper wrapper = new CustomHttpServletRequestWrapper(httpRequest);
            chain.doFilter(wrapper, res);
        } else {
            chain.doFilter(req, res);
        }
    }

    class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

        public CustomHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            return AUTHORIZATION_HEADER.equals(name) ? null : super.getHeader(name);
        }
    } 
}
