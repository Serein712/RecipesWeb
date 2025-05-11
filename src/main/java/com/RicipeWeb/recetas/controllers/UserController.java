package com.RicipeWeb.recetas.controllers;


import com.RicipeWeb.recetas.dtos.UserProfileDTO;
import com.RicipeWeb.recetas.dtos.UserRegisterDTO;
import com.RicipeWeb.recetas.dtos.UserUpdateDTO;
import com.RicipeWeb.recetas.models.User;
import com.RicipeWeb.recetas.repositories.UserRepository;
import com.RicipeWeb.recetas.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO dto) {
        boolean success = userService.registerUser(dto);

        if (success) {
            return ResponseEntity.ok("Usuario registrado con éxito");
        } else {
            return ResponseEntity.badRequest().body("El nombre de usuario o email ya está en uso");
        }
    }

    /*@GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }

        String email = auth.getName(); // el email se usó como "username" en UserDetails

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        UserMeDTO dto = new UserMeDTO(user.getUserId(), user.getUsername(), user.getEmail());

        return ResponseEntity.ok(dto);
    }*/
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setName(user.getUsername()); // si tienes nombre
        //dto.setCreatedAt(user.getCreatedAt()); // si tienes fecha de registro

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody UserUpdateDTO dto, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está en uso");
        }

        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            if (dto.getCurrentPassword() == null || !passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
                return ResponseEntity.badRequest().body("La contraseña actual no es válida");
            }
            user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        }

        user.setEmail(dto.getEmail());
        user.setUsername(dto.getName());

        userRepository.save(user);
        return ResponseEntity.ok("Perfil actualizado");
    }
}