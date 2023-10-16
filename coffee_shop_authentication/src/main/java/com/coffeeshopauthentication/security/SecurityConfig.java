package com.coffeeshopauthentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private RolesClaimConverter rolesClaimConverter;

    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    @Autowired
    private RemoveTokenFilter removeTokenFilter;

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }
    
    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .addFilterBefore(removeTokenFilter, BearerTokenAuthenticationFilter.class)
            .oauth2ResourceServer(
                resourceServer -> {
                    resourceServer.jwt().jwtAuthenticationConverter(rolesClaimConverter);
                }
            )
            .logout()
                .addLogoutHandler(customLogoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            .and()
            .authorizeHttpRequests()
                .requestMatchers("/**")
                    .permitAll()
            .anyRequest()
                .authenticated();
        return http.build();
    }
}