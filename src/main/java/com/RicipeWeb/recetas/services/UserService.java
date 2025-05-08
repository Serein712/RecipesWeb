package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.UserRegisterDTO;
import com.RicipeWeb.recetas.models.User;
import com.RicipeWeb.recetas.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean registerUser(UserRegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()) || userRepository.existsByEmail(dto.getEmail())) {
            return false;
        }
        User user = modelMapper.map(dto, User.class);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean validateLogin(String email, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }
}