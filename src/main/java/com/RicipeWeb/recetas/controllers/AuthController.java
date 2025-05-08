package com.RicipeWeb.recetas.controllers;

import com.RicipeWeb.recetas.dtos.LoginRequestDTO;
import com.RicipeWeb.recetas.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginDTO) {
        boolean success = userService.validateLogin(loginDTO.getEmail(), loginDTO.getPassword());

        if (success) {
            return ResponseEntity.ok("Login correcto");
        } else {
            return ResponseEntity.status(401).body("Email o contraseña incorrectos");
        }
    }
}
