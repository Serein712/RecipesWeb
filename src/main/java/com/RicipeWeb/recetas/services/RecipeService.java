package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.IngredientDTO;
import com.RicipeWeb.recetas.dtos.RecipeIngredientsDTO;
import com.RicipeWeb.recetas.dtos.RecipeDTO;
import com.RicipeWeb.recetas.dtos.RecipeRequestDTO;
import com.RicipeWeb.recetas.models.*;
import com.RicipeWeb.recetas.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    @Autowired
    private UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository,
                         CategoryRepository categoryRepository,
                         IngredientRepository ingredientRepository,
                         UnitRepository unitRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
    }

    public RecipeDTO createRecipe(RecipeRequestDTO dto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        recipe.setPrepTime(dto.getPrepTime());
        recipe.setCookTime(dto.getCookTime());
        recipe.setServings(dto.getServings());
        User author = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        recipe.setAuthor(author);
        // Categorías
        Set<Category> categories = new HashSet<>();
        for (Long categoryId : dto.getCategoryIds()) {
            categoryRepository.findById(categoryId).ifPresent(categories::add);
        }
        recipe.setCategories(categories);

        // Guardamos receta base primero (para poder asociar ingredientes que dependen de su ID)
        Recipe savedRecipe = recipeRepository.save(recipe);

        // Ingredientes
        for (RecipeIngredientsDTO ingredientDTO : dto.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(ingredientDTO.getIngredientId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

            Unit unit = unitRepository.findById(ingredientDTO.getUnitId())
                    .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));

            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(savedRecipe);
            ri.setIngredient(ingredient);
            ri.setUnit(unit);
            ri.setQuantity(ingredientDTO.getQuantity());

            savedRecipe.getRecipeIngredients().add(ri); // Asegúrate de tener el set creado en la entidad
        }

        // Guardamos otra vez para que se guarden los ingredientes relacionados
        Recipe finalRecipe = recipeRepository.save(savedRecipe);

        // Convertimos a DTO
        return convertToDTO(finalRecipe);
    }

    private RecipeDTO convertToDTO(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getRecipeId());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setCookTime(recipe.getCookTime());
        dto.setServings(recipe.getServings());

        List<String> categoryNames = recipe.getCategories()
                .stream()
                .map(Category::getName)
                .toList();
        dto.setCategoryNames(categoryNames);

        return dto;
    }
}
