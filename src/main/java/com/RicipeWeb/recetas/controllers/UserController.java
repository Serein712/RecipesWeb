package com.RicipeWeb.recetas.controllers;

import com.RicipeWeb.recetas.dtos.UserRegisterDTO;
import com.RicipeWeb.recetas.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO dto) {
        boolean success = userService.registerUser(dto);

        if (success) {
            return ResponseEntity.ok("Usuario registrado con éxito");
        } else {
            return ResponseEntity.badRequest().body("El nombre de usuario o email ya está en uso");
        }
    }
}