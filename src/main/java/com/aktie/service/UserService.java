package com.aktie.service;

import com.aktie.dto.UserDTO;
import com.aktie.model.EnumRole;
import com.aktie.model.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {
    
    @Transactional
    public void create(UserDTO dto) {

        var user = new User();

        user.setEmail(dto.email());
        user.setName(dto.name());
        user.setPassword(dto.password());
        user.setRole(EnumRole.CUSTOMER);

        user.persist();
    }

}
