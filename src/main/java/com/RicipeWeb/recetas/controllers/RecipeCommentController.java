package com.RicipeWeb.recetas.controllers;

import com.RicipeWeb.recetas.dtos.RecipeCommentCreateDTO;
import com.RicipeWeb.recetas.dtos.RecipeCommentDTO;
import com.RicipeWeb.recetas.services.RecipeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RecipeCommentController {

    @Autowired
    private RecipeCommentService commentService;

    // Comentarios de una receta
    @GetMapping("/recipes/{id}/comments")
    public ResponseEntity<List<RecipeCommentDTO>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsForRecipe(id));
    }

    // Promedio de valoraciones
    @GetMapping("/recipes/{id}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getAverageRatingForRecipe(id));
    }

    // Crear comentario
    @PostMapping("/recipes/{id}/comments")
    public ResponseEntity<RecipeCommentDTO> createComment(
            @PathVariable Long id,
            @RequestBody RecipeCommentCreateDTO dto,
            Principal principal) {

        RecipeCommentDTO created = commentService.createComment(id, principal.getName(), dto);
        return ResponseEntity.ok(created);
    }

    // Editar comentario (solo autor)
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<RecipeCommentDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody RecipeCommentCreateDTO dto,
            Principal principal) {

        RecipeCommentDTO updated = commentService.updateComment(commentId, principal.getName(), dto);
        return ResponseEntity.ok(updated);
    }

    // Borrar comentario (solo autor)
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            Principal principal) {

        commentService.deleteComment(commentId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}