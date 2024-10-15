package com.devx.api_gateway.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.session.InMemoryReactiveSessionRegistry;
import org.springframework.security.core.session.ReactiveSessionRegistry;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.SessionLimit;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                     ReactiveAuthenticationManager authenticationManager,
                                                     ServerAuthenticationConverter authenticationConverter) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

        return http
                .authorizeExchange(exchanges -> exchanges
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/auth/**")).permitAll()
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/review/add")).permitAll()
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/review/all")).permitAll()
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/booking/temp/add")).permitAll()
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/menu/all")).permitAll()
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/order/add")).permitAll()
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/room-type/available-count")).permitAll()
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/contact-us/support-ticket/add")).permitAll()
                        .matchers(ServerWebExchangeMatchers.pathMatchers("/api/table/by-id/**")).permitAll()
                        .anyExchange().authenticated()
                )
                .sessionManagement((sessions) ->
                        sessions.concurrentSessions((concurrency) ->
                                concurrency.maximumSessions(SessionLimit.of(1))))
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .build();
    }

    @Bean
    ReactiveSessionRegistry reactiveSessionRegistry() {
        return new InMemoryReactiveSessionRegistry();
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("PATCH");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("OPTIONS");
        corsConfig.setAllowedOriginPatterns(List.of("http://localhost:[*]"));
        corsConfig.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "User-Key", "Request-Tracker", "Session-Tracker", "X-XSRF-TOKEN", "X-IBM-CLIENT-ID", "Message-ID", "X-IBM-CLIENT-SECRET"));
        corsConfig.setExposedHeaders(Arrays.asList("X-Get-Header"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        return new CorsWebFilter(corsConfiguration());
    }
}
