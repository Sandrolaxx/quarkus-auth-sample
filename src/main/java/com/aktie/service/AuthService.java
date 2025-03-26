package com.aktie.service;

import java.util.UUID;

import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;

import com.aktie.auth.TokenUtils;
import com.aktie.dto.LoginDTO;
import com.aktie.dto.TokenDTO;
import com.aktie.exception.CustomException;
import com.aktie.model.EnumErrorCode;
import com.aktie.model.EnumRole;
import com.aktie.model.User;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * 
 * @author SRamos
 */
@ApplicationScoped
public class AuthService {

    private static Integer DEFAULT_EXPIRATION_TIME = 60;

    public TokenDTO genToken(LoginDTO dto) {

        var user = (User) User.find("email", dto.email())
                .firstResultOptional()
                .orElseThrow(() -> new CustomException(EnumErrorCode.USUARIO_NAO_ENCONTRADO));

        if (!dto.password().equals(user.getPassword())) {
            throw new CustomException(EnumErrorCode.ERRO_LOGIN);
        }

        return generateToken(user.getEmail(), user.getRole());
    }

    public TokenDTO generateToken(String email, EnumRole role) {
        try {
            JwtClaims jwtClaims = new JwtClaims();

            jwtClaims.setIssuer("SAMPLE-JWT-API");
            jwtClaims.setJwtId(UUID.randomUUID().toString());
            jwtClaims.setClaim(Claims.preferred_username.name(), email);
            jwtClaims.setClaim(Claims.groups.name(), role.getKey());
            jwtClaims.setAudience("using-jwt");
            jwtClaims.setExpirationTimeMinutesInTheFuture(DEFAULT_EXPIRATION_TIME);

            TokenDTO token = new TokenDTO(TokenUtils.generateTokenString(jwtClaims), DEFAULT_EXPIRATION_TIME);

            return token;
        } catch (Exception e) {
            throw new CustomException(EnumErrorCode.ERRO_LOGIN_INTERNO);
        }
    }

}
