package com.cofeeshop.security;

import static com.cofeeshop.dto.UserType.SHOP_CHAIN_OWNER;
import static com.cofeeshop.dto.UserType.SHOP_OPERATOR;

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
                .requestMatchers("/shop-chain/**")
                    .hasAnyAuthority(SHOP_CHAIN_OWNER.getAuthorityName())
                .requestMatchers("/shop/**")
                    .hasAnyAuthority(SHOP_CHAIN_OWNER.getAuthorityName(), SHOP_OPERATOR.getAuthorityName())
                .requestMatchers("/contact/**")
                    .hasAnyAuthority(SHOP_CHAIN_OWNER.getAuthorityName(), SHOP_OPERATOR.getAuthorityName())
                .requestMatchers("/dish/**")
                    .hasAnyAuthority(SHOP_CHAIN_OWNER.getAuthorityName(), SHOP_OPERATOR.getAuthorityName())
                .requestMatchers("/order-queue/**")
                    .hasAnyAuthority(SHOP_CHAIN_OWNER.getAuthorityName(), SHOP_OPERATOR.getAuthorityName())
                .requestMatchers("/*internal/**")
                    .permitAll()
                .requestMatchers("/public/**")
                    .permitAll()
                .requestMatchers("/actuator/**")
                    .permitAll()
            .anyRequest()
                .authenticated();
        return http.build();
    }
}