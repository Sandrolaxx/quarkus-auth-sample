package com.aktie.service;

import java.util.Arrays;
import java.util.UUID;

import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;

import com.aktie.dto.LoginDTO;
import com.aktie.dto.TokenDTO;
import com.aktie.exception.CustomException;
import com.aktie.exception.EnumErrorCode;
import com.aktie.model.User;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author SRamos
 */
@ApplicationScoped
public class AuthService {

    private static Integer DEFAULT_EXPIRATION_TIME = 60;

    public TokenDTO genToken(LoginDTO dto) {

        // var user = pgfindUserBy.execute(dto.email());
        var user = new User();

        if (user == null) {
            throw new CustomException(EnumErrorCode.USUARIO_NAO_ENCONTRADO);
        }

        if (user.getPassword() == null) {
            throw new CustomException(EnumErrorCode.USUARIO_CADASTRADO_SEM_SENHA);
        }

        if (!dto.password().equals(user.getPassword())) {
            throw new CustomException(EnumErrorCode.ERRO_LOGIN);
        }

        return generateToken(user.getEmail(), user.getRoles());
    }

    public TokenDTO generateToken(String email, String... roles) {
        try {
            JwtClaims jwtClaims = new JwtClaims();
            
            jwtClaims.setIssuer("SAMPLE-JWT-API");
            jwtClaims.setJwtId(UUID.randomUUID().toString());
            jwtClaims.setClaim(Claims.preferred_username.name(), email);
            jwtClaims.setClaim(Claims.groups.name(), Arrays.asList(roles));
            jwtClaims.setAudience("using-jwt");
            jwtClaims.setExpirationTimeMinutesInTheFuture(DEFAULT_EXPIRATION_TIME);
            
            TokenDTO token = new TokenDTO(TokenUtils.generateTokenString(jwtClaims), DEFAULT_EXPIRATION_TIME);

            return token;
        } catch (Exception e) {
            throw new CustomException(EnumErrorCode.ERRO_LOGIN_INTERNO);
        }
    }

}
