package com.library.security.filter;

import com.library.security.jwt.ValidateToken;
import com.library.utils.ParseClaims;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    @Value("${jwt.at.secret}")
    private String ATSecret;

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(AUTHORIZATION);
        if (null != jwt) {
            String AccessToken = jwt.split(" ")[1].trim();

            if (!ValidateToken.validate(AccessToken)) {
                response.sendError(401, "Invalid credentials");
                return;
            }

            Claims claims = ParseClaims.parseClaims(ATSecret, AccessToken);
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            String username = String.valueOf(claims.get("username"));
            String authorities = (String) claims.get("authorities");
            Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/v1/auth/refresh") ||
                request.getServletPath().equals("/api/v1/auth/login") ||
                request.getServletPath().equals("/api/v1/auth/register") ||
                request.getServletPath().equals("/api/v1/auth/resetPassword") ||
                request.getServletPath().equals("/api/v1/auth/changePassword");
    }
}
