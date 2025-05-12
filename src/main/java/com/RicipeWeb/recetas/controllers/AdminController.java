package com.RicipeWeb.recetas.controllers;

import com.RicipeWeb.recetas.dtos.AdminStatsDTO;
import com.RicipeWeb.recetas.dtos.ModerationCommentDTO;
import com.RicipeWeb.recetas.dtos.UserActivityDTO;
import com.RicipeWeb.recetas.dtos.UserAdminDTO;
import com.RicipeWeb.recetas.models.RecipeComment;
import com.RicipeWeb.recetas.models.User;
import com.RicipeWeb.recetas.repositories.RecipeCommentRepository;
import com.RicipeWeb.recetas.repositories.RecipeRepository;
import com.RicipeWeb.recetas.repositories.UserRepository;
import com.RicipeWeb.recetas.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeCommentRepository commentRepository;
    private final AdminService adminService;

    public AdminController(UserRepository userRepository, RecipeRepository recipeRepository, RecipeCommentRepository commentRepository, AdminService adminService) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<UserAdminDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user ->
                new UserAdminDTO(
                        user.getUserId(),
                        user.getEmail(),
                        user.getRole().name(),
                        recipeRepository.countByAuthor(user)
                )
        ).toList();
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @RequestParam String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        user.setRole(User.Role.valueOf(role.toUpperCase()));
        userRepository.save(user);
        return ResponseEntity.ok("Rol actualizado");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDTO> getStats() {
        long totalUsers = userRepository.count();
        long totalRecipes = recipeRepository.count();
        long totalComments = commentRepository.count();

        List<UserActivityDTO> topAuthors = userRepository.findAll().stream()
                .map(u -> new UserActivityDTO(u.getEmail(), recipeRepository.countByAuthor(u)))
                .sorted((a, b) -> Long.compare(b.getRecipeCount(), a.getRecipeCount()))
                .limit(5)
                .toList();

        // Simulación de registros por mes (si no tienes campo 'createdAt', puedes omitir esto)
        Map<String, Long> registrations = userRepository.findAll().stream()
                .filter(u -> u.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        u -> u.getCreatedAt().getMonth().toString(),
                        Collectors.counting()
                ));

        AdminStatsDTO stats = new AdminStatsDTO(totalUsers, totalRecipes, totalComments, topAuthors, registrations);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<ModerationCommentDTO>> getAllComments() {
        List<RecipeComment> comments = commentRepository.findAllByOrderByCreatedAtDesc();

        List<ModerationCommentDTO> dtos = comments.stream().map(c -> new ModerationCommentDTO(
                c.getId(),
                c.getRecipe().getTitle(),
                c.getAuthor().getEmail(),
                c.getRating(),
                c.getText(),
                c.getCreatedAt()
        )).toList();

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        if (!commentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}