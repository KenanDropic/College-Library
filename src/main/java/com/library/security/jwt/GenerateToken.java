package com.library.security.jwt;

import com.library.entity.SecurityUser;
import com.library.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class GenerateToken {
    @Value("${jwt.at.secret}")
    private String ATSecret;
    @Value("${jwt.rt.secret}")
    private String RTSecret;

    public String generateAToken(User user) {
        SecurityUser securityUser = new SecurityUser(user);
        System.out.println("Generated authorities:" + securityUser.getAuthorities());

        // expires in 10 minutes
        return Jwts.builder()
                .setIssuer("host:8080")
                .setSubject("JWT")
                .claim("username", securityUser.getUsername())
                .claim("authorities", populateAuthorities((securityUser.getAuthorities())))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, ATSecret)
                .compact();

    }

    // expires in 5 days
    public String generateRToken(User user) {
        SecurityUser securityUser = new SecurityUser(user);

        return Jwts.builder()
                .setIssuer("localhost:8080")
                .setSubject("JWT-RT")
                .claim("username", securityUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, RTSecret)
                .compact();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        List<String> authoritiesList = new ArrayList<>();
        for (GrantedAuthority authority : collection) {
            authoritiesList.add(authority.getAuthority());
        }
        return String.join(",", authoritiesList);
    }
}
