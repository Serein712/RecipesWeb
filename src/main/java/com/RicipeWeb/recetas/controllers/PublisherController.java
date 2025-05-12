package com.RicipeWeb.recetas.controllers;

import com.RicipeWeb.recetas.dtos.PublisherCommentDTO;
import com.RicipeWeb.recetas.dtos.PublisherProfileDTO;
import com.RicipeWeb.recetas.dtos.RecipeSummaryDTO;
import com.RicipeWeb.recetas.models.Recipe;
import com.RicipeWeb.recetas.models.RecipeComment;
import com.RicipeWeb.recetas.models.User;
import com.RicipeWeb.recetas.repositories.RecipeCommentRepository;
import com.RicipeWeb.recetas.repositories.RecipeRepository;
import com.RicipeWeb.recetas.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeCommentRepository commentRepository;

    public PublisherController(UserRepository userRepository, RecipeRepository recipeRepository, RecipeCommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherProfileDTO> getPublisherProfile(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (user.getRole() != User.Role.PUBLISHER) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Este autor no tiene perfil público");
        }

        List<Recipe> recipes = recipeRepository.findByAuthor(user);
        double averageRating = recipes.stream()
                .flatMap(r -> r.getComments().stream())
                .mapToInt(RecipeComment::getRating)
                .average()
                .orElse(0.0);

        List<RecipeSummaryDTO> recipeSummaries = recipes.stream()
                .map(r -> {
                    double avgRating = r.getComments().stream()
                            .mapToInt(RecipeComment::getRating)
                            .average()
                            .orElse(0.0);
                    return new RecipeSummaryDTO(
                            r.getRecipeId(),
                            r.getTitle(),
                            r.getImageUrl(),
                            r.getDescription(),
                            avgRating
                    );
                })
                .toList();

        List<PublisherCommentDTO> recentComments = commentRepository
                .findTop10ByRecipeAuthorOrderByCreatedAtDesc(user)
                .stream()
                .map(c -> new PublisherCommentDTO(
                        c.getRecipe().getTitle(),
                        c.getRecipe().getRecipeId(),
                        c.getText(),
                        c.getRating(),
                        c.getAuthor().getUsername()
                ))
                .toList();

        PublisherProfileDTO dto = new PublisherProfileDTO(
                user.getUsername(),
                user.getEmail(),
                user.getBio(),
                averageRating,
                recipeSummaries,
                recentComments
        );

        return ResponseEntity.ok(dto);
    }
}