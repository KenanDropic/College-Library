package com.library.security.filter;

import com.library.security.jwt.ValidateToken;
import com.library.service.AuthService;
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
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTRTTokenValidatorFilter extends OncePerRequestFilter {
    @Value("${jwt.rt.secret}")
    private String RTSecret;

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        if (!request.getServletPath().equals("/api/v1/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie cookie = WebUtils.getCookie(request, "jwt-rt");

        if (cookie == null) {
            // rt token is invalid,clear user & clear cookie
            AuthService.ClearAuth(response);
        }

        assert cookie != null;
        String cookieValue = cookie.getValue();

        if (!ValidateToken.validate(cookieValue)) {
            response.sendError(401,"Invalid credentials!");
            return;
        }

        Claims claims = ParseClaims.parseClaims(RTSecret,cookieValue);
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        String username = String.valueOf(claims.get("username"));
        String authorities = (String) claims.get("authorities");
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }


}
