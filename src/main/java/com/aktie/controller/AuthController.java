package com.aktie.controller;

import com.aktie.dto.LoginDTO;
import com.aktie.dto.TokenDTO;
import com.aktie.service.AuthService;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("api/v1/auth")
public class AuthController {

    @Inject
    AuthService service;
    
    @POST
    public TokenDTO login(LoginDTO dto) {
        return service.genToken(dto);
    }

}
