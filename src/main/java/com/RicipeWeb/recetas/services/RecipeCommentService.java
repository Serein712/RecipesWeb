package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.RecipeCommentCreateDTO;
import com.RicipeWeb.recetas.dtos.RecipeCommentDTO;
import com.RicipeWeb.recetas.models.*;
import com.RicipeWeb.recetas.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
public class RecipeCommentService {

    @Autowired private RecipeRepository recipeRepository;
    @Autowired private RecipeCommentRepository commentRepository;
    @Autowired private UserRepository userRepository;

    public List<RecipeCommentDTO> getCommentsForRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Receta no encontrada"));

        return commentRepository.findByRecipe(recipe)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecipeCommentDTO createComment(Long recipeId, String userEmail, RecipeCommentCreateDTO dto) {
        if (dto.getText() == null || dto.getText().isBlank() || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ResponseStatusException(BAD_REQUEST, "Texto y puntuación (1–5) son obligatorios");
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Receta no encontrada"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Usuario no válido"));

        if (commentRepository.existsByRecipeAndAuthor(recipe, user)) {
            throw new ResponseStatusException(CONFLICT, "Ya has comentado esta receta");
        }

        RecipeComment comment = new RecipeComment();
        comment.setRecipe(recipe);
        comment.setAuthor(user);
        comment.setRating(dto.getRating());
        comment.setText(dto.getText());

        return toDTO(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId, String userEmail) {
        RecipeComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comentario no encontrado"));

        if (!comment.getAuthor().getEmail().equals(userEmail)) {
            throw new ResponseStatusException(FORBIDDEN, "No puedes eliminar este comentario");
        }

        commentRepository.delete(comment);
    }

    public RecipeCommentDTO updateComment(Long commentId, String userEmail, RecipeCommentCreateDTO dto) {
        RecipeComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comentario no encontrado"));

        if (!comment.getAuthor().getEmail().equals(userEmail)) {
            throw new ResponseStatusException(FORBIDDEN, "No puedes editar este comentario");
        }

        if (dto.getText() == null || dto.getText().isBlank() || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ResponseStatusException(BAD_REQUEST, "Texto y puntuación válidos son obligatorios");
        }

        comment.setText(dto.getText());
        comment.setRating(dto.getRating());
        return toDTO(commentRepository.save(comment));
    }

    public double getAverageRatingForRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Receta no encontrada"));

        List<RecipeComment> comments = commentRepository.findByRecipe(recipe);
        if (comments.isEmpty()) return 0;

        return comments.stream().mapToInt(RecipeComment::getRating).average().orElse(0);
    }

    private RecipeCommentDTO toDTO(RecipeComment comment) {
        RecipeCommentDTO dto = new RecipeCommentDTO();
        dto.setId(comment.getId());
        dto.setAuthorName(comment.getAuthor().getUsername() != null ? comment.getAuthor().getUsername() : comment.getAuthor().getEmail());
        dto.setAuthorEmail(comment.getAuthor().getEmail());
        dto.setText(comment.getText());
        dto.setRating(comment.getRating());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}