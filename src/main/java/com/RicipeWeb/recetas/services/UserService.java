package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.UserRegisterDTO;
import com.RicipeWeb.recetas.models.User;
import com.RicipeWeb.recetas.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public boolean registerUser(UserRegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()) || userRepository.existsByEmail(dto.getEmail())) {
            return false;
        }

        User user = modelMapper.map(dto, User.class);
        user.setPasswordHash(encryptPassword(dto.getPassword()));

        userRepository.save(user);
        return true;
    }

    private String encryptPassword(String password) {
        // Simplificado: usar bcrypt real en futuro
        return Integer.toHexString(password.hashCode());
    }
}