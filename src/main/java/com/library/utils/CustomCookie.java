package com.library.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomCookie {
    private String CookieName;
    private String CookieValue;

    public static ResponseCookie AssignCookie(String CookieName, String Value){
        return ResponseCookie.from(CookieName,Value)
                .httpOnly(true)
                .sameSite("none")
                .secure(true)
                .path("/")
                .build();
    }

    public static ResponseCookie ClearCookie(String CookieName){
        return ResponseCookie.from(CookieName,null)
                .maxAge(0)
                .httpOnly(true)
                .sameSite("none")
                .secure(true)
                .path("/")
                .build();
    }
}
