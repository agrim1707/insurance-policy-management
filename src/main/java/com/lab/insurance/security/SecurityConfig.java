package com.lab.insurance.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration:
 *  - HTTP Basic authentication backed by {@link CustomUserDetailsService}
 *    (registered users live in the database, admin is seeded by {@link AdminSeed}).
 *  - Role based authorization: only ADMIN can reach /api/**, only USER
 *    can purchase policies and raise claims.
 *  - Stateless (no HTTP session - every request must carry credentials).
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   // REST API - CSRF tokens are not needed
                // allow the H2 web console to render inside a frame
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // public: registration and browsing policies
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/policies/**").permitAll()
                        // logged-in user: my profile / my policies / my claims
                        .requestMatchers("/api/me/**").authenticated()
                        // USER-only actions: purchase a policy and submit a claim
                        .requestMatchers(HttpMethod.POST, "/api/policies/*/purchase").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/claims").hasRole("USER")
                        // admin review endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // everything else needs a logged in user
                        .anyRequest().authenticated())
                .httpBasic(basic -> basic.realmName("insurance-lab"));

        return http.build();
    }
}
