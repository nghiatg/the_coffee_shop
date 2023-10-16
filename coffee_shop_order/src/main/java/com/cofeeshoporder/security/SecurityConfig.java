package com.cofeeshoporder.security;

import static com.cofeeshoporder.dto.UserType.CUSTOMER;
import static com.cofeeshoporder.dto.UserType.SHOP_CHAIN_OWNER;
import static com.cofeeshoporder.dto.UserType.SHOP_OPERATOR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private RolesClaimConverter rolesClaimConverter;

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
            .authorizeHttpRequests()
                .requestMatchers("/manager/order/**")
                    .hasAnyAuthority(SHOP_CHAIN_OWNER.getAuthorityName(), SHOP_OPERATOR.getAuthorityName())
                .requestMatchers("/customer/order/**")
                    .hasAnyAuthority(CUSTOMER.getAuthorityName())
                .requestMatchers("/actuator/**")
                    .permitAll()
                .requestMatchers("/public/**")
                    .permitAll()
            .anyRequest()
                .authenticated();
        return http.build();
    }
}