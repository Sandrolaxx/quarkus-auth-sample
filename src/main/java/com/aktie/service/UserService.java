package com.aktie.service;

import java.util.UUID;

import com.aktie.dto.UserDTO;
import com.aktie.exception.CustomException;
import com.aktie.model.EnumErrorCode;
import com.aktie.model.EnumRole;
import com.aktie.model.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {
    
    @Transactional
    public void create(UserDTO dto) {

        var isInvalidEmail = User.find("email", dto.email()).count() > 0;

        if (isInvalidEmail) {
            throw new CustomException(EnumErrorCode.EMAIL_INVALIDO);
        }

        var user = new User();

        user.setEmail(dto.email());
        user.setName(dto.name());
        user.setPassword(dto.password());
        user.setRole(EnumRole.CUSTOMER);

        user.persist();
    }

    public UserDTO findById(UUID uuid) {
        User entity = User.findById(uuid);

        return new UserDTO(entity.getName(), entity.getEmail(), null);
    }

}
