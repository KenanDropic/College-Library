package com.library.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ParseClaims {

    private String secret;
    private String value;

    public ParseClaims(String secret, String value) {
        this.secret = secret;
        this.value = value;
    }

    public static Claims parseClaims(String secret, String value) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(value)
                .getBody();
    }

}
