package com.library.security;

import com.library.security.filter.JWTRTTokenValidatorFilter;
import com.library.security.filter.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class WebSecurityConfig {

    private final JWTTokenValidatorFilter jwtTokenValidatorFilter;
    private final JWTRTTokenValidatorFilter jwtrtTokenValidatorFilter;

    public WebSecurityConfig(JWTTokenValidatorFilter jwtTokenValidatorFilter,
                             JWTRTTokenValidatorFilter jwtrtTokenValidatorFilter) {
        this.jwtTokenValidatorFilter = jwtTokenValidatorFilter;
        this.jwtrtTokenValidatorFilter = jwtrtTokenValidatorFilter;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/api/v1/**")
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .cors()
                .configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
                    config.setMaxAge(3600L);
                    return config;
                }).and().csrf().disable()
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("jwt-rt"))
                .authorizeHttpRequests((auth) -> auth
                        .antMatchers("/api/v1/auth/register", "/api/v1/auth/login",
                                "/api/v1/auth/resetPassword", "/api/v1/auth/changePassword").permitAll()
                        .antMatchers("/api/v1/auth/resendConfirmToken").hasAnyRole("ADMIN", "USER", "LIBRARIAN")
                        .antMatchers(POST, "/api/v1/books").hasAnyRole("LIBRARIAN", "ADMIN")
                        .antMatchers(PUT, "/api/v1/books/{id}").hasAnyRole("LIBRARIAN", "ADMIN")
                        .antMatchers(DELETE, "/api/v1/books/{id}").hasRole("ADMIN")
                        .antMatchers(PUT, "//api/v1/books/writeOff/{id}").hasRole("ADMIN")
                        .antMatchers(POST, "/api/v1/loans").hasAnyRole("LIBRARIAN", "ADMIN")
                        .antMatchers(PUT, "/api/v1/loans/{id}").hasAnyRole("LIBRARIAN", "ADMIN")
                        .antMatchers(DELETE, "/api/v1/loans/{id}").hasRole("ADMIN")
                        .antMatchers("/api/v1/users/**").hasRole("ADMIN")
                        .antMatchers(GET, "/api/v1/users/{id}/loans").hasAnyRole("ADMIN", "USER", "LIBRARIAN")
                )
                .addFilterBefore(jwtTokenValidatorFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtrtTokenValidatorFilter, BasicAuthenticationFilter.class)
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
