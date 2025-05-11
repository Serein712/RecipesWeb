package com.RicipeWeb.recetas.controllers;

import com.RicipeWeb.recetas.dtos.RecipeRequestDTO;
import com.RicipeWeb.recetas.dtos.RecipeDTO;
import com.RicipeWeb.recetas.dtos.RecipeSummaryDTO;
import com.RicipeWeb.recetas.models.Recipe;
import com.RicipeWeb.recetas.repositories.RecipeRepository;
import com.RicipeWeb.recetas.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeController(RecipeService recipeService, RecipeRepository recipeRepository) {
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
    }

    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeRequestDTO recipeRequestDTO, Principal principal) {
        System.out.println("Usuario autenticado: " + SecurityContextHolder.getContext().getAuthentication().getName());

        String email = principal.getName(); // ← este es el usuario autenticado extraído del token
        RecipeDTO createdRecipe = recipeService.createRecipe(recipeRequestDTO, email);
        return ResponseEntity.ok(createdRecipe);
    }

    /*@GetMapping
    public ResponseEntity<List<RecipeSummaryDTO>> getAllRecipes() {
        List<RecipeSummaryDTO> recipeSummaries = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipeSummaries);
    }*/

    @GetMapping
    public ResponseEntity<List<RecipeSummaryDTO>> getAllRecipes(
            @RequestParam(required = false) String author) {

        List<RecipeSummaryDTO> recipes = (author == null || author.isBlank())
                ? recipeService.getAllRecipes()
                : recipeService.getRecipesByAuthor(author);

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receta no encontrada"));

        RecipeDTO dto = recipeService.convertToDTO(recipe);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(
            @PathVariable Long id,
            @RequestBody RecipeRequestDTO dto,
            Principal principal) {

        String email = principal.getName(); // extraído del JWT
        RecipeDTO updated = recipeService.updateRecipe(id, dto, email);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        recipeService.deleteRecipe(id, email);
        return ResponseEntity.noContent().build();
    }

}
