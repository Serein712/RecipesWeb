package com.RicipeWeb.recetas.controllers;

import com.RicipeWeb.recetas.dtos.RecipeRequestDTO;
import com.RicipeWeb.recetas.dtos.RecipeDTO;
import com.RicipeWeb.recetas.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeRequestDTO recipeRequestDTO, Principal principal) {
        System.out.println("Usuario autenticado: " + SecurityContextHolder.getContext().getAuthentication().getName());

        String email = principal.getName(); // ← este es el usuario autenticado extraído del token
        RecipeDTO createdRecipe = recipeService.createRecipe(recipeRequestDTO, email);
        return ResponseEntity.ok(createdRecipe);
    }
}
