package com.aktie.auth;

import com.aktie.model.EnumRole;

import io.smallrye.jwt.build.Jwt;

/**
 * @author SRamos
 */
public class GenerateToken {

    private static Integer DEFAULT_EXPIRATION_TIME = 720;

    public static String generate(String email, String userId, EnumRole role) {
        return Jwt.issuer("SAMPLE-JWT-API")
                .upn(email)
                .groups(role.getKey())
                .claim("uuid", userId)
                .audience("user-service")
                .expiresIn(DEFAULT_EXPIRATION_TIME)
                .sign();
    }

}
