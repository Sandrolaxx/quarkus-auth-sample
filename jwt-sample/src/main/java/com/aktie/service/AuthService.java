package com.aktie.service;

import com.aktie.auth.GenerateToken;
import com.aktie.dto.LoginDTO;
import com.aktie.dto.TokenDTO;
import com.aktie.exception.CustomException;
import com.aktie.model.EnumErrorCode;
import com.aktie.model.User;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * 
 * @author SRamos
 */
@ApplicationScoped
public class AuthService {

    private static Integer DEFAULT_EXPIRATION_TIME = 720;

    public TokenDTO genToken(LoginDTO dto) {

        var user = (User) User.find("email", dto.email())
                .firstResultOptional()
                .orElseThrow(() -> new CustomException(EnumErrorCode.USUARIO_NAO_ENCONTRADO));

        if (!dto.password().equals(user.getPassword())) {
            throw new CustomException(EnumErrorCode.ERRO_LOGIN);
        }

        //Fluxo com TokenUtils
        // var jwt = TokenUtils.generateToken(user.getEmail(), user.getId().toString(), user.getRole());
        var jwt = GenerateToken.generate(user.getEmail(), user.getId().toString(), user.getRole());

        return new TokenDTO(jwt, DEFAULT_EXPIRATION_TIME);
    }

}
