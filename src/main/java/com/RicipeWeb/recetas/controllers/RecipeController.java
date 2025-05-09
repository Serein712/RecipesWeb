package com.RicipeWeb.recetas.controllers;

import com.RicipeWeb.recetas.dtos.RecipeRequestDTO;
import com.RicipeWeb.recetas.dtos.RecipeDTO;
import com.RicipeWeb.recetas.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeRequestDTO recipeRequestDTO) {
        RecipeDTO createdRecipe = recipeService.createRecipe(recipeRequestDTO);
        return ResponseEntity.ok(createdRecipe);
    }
}
