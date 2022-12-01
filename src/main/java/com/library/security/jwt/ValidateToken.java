package com.library.security.jwt;

import com.auth0.jwt.JWT;

import java.util.Calendar;

public class ValidateToken {

    public static boolean validate(String token){
        try{
            long expiresAt = JWT.decode(token).getExpiresAt().getTime();
            Calendar cal = Calendar.getInstance();

            if(expiresAt >= cal.getTime().getTime()){
                return true;
            }
        }catch (IllegalArgumentException e) {
            System.out.printf("JWT is invalid - {%s}%n",e.getMessage());
            return false;
        }
        return false;
    }
}
