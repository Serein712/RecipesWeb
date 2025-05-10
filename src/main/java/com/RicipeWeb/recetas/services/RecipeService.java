package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.*;
import com.RicipeWeb.recetas.models.*;
import com.RicipeWeb.recetas.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
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

    public RecipeDTO createRecipe(RecipeRequestDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Recipe recipe = new Recipe();
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        recipe.setPrepTime(dto.getPrepTime());
        recipe.setCookTime(dto.getCookTime());
        recipe.setServings(dto.getServings());
        recipe.setImageUrl(dto.getImageUrl());
        recipe.setAuthor(user);

        // Categorías
        Set<Category> categories = new HashSet<>();
        for (Long categoryId : dto.getCategories()) {
            categoryRepository.findById(categoryId).ifPresent(categories::add);
        }
        recipe.setCategories(categories);

        // Guardamos receta base primero (para poder asociar ingredientes que dependen de su ID)
        Recipe savedRecipe = recipeRepository.save(recipe);

        // Ingredientes
        for (RecipeIngredientsDTO ingredientDTO : dto.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(ingredientDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

            Unit unit = unitRepository.findById(ingredientDTO.getUnit_id())
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

        // Pasos
        List<RecipeStep> stepList = new ArrayList<>();
        int index = 1;
        for (String stepText : dto.getSteps()) {
            RecipeStep step = new RecipeStep();
            step.setInstruction(stepText);
            step.setStep_number(index++); // opcional si tienes un campo para orden
            step.setRecipe(savedRecipe); // relación bidireccional
            stepList.add(step);
        }
        //savedRecipe.setSteps(stepList);
        savedRecipe.getSteps().clear();
        savedRecipe.getSteps().addAll(stepList);

        // Guardamos otra vez
        finalRecipe = recipeRepository.save(savedRecipe);

        // Convertimos a DTO
        return convertToDTO(finalRecipe);
    }

    public RecipeDTO updateRecipe(Long recipeId, RecipeRequestDTO dto, String email) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receta no encontrada"));

        // 🔒 Verificación de autor
        if (!recipe.getAuthor().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No eres el autor de esta receta");
        }

        // 🛠 Actualización de campos
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        recipe.setPrepTime(dto.getPrepTime());
        recipe.setCookTime(dto.getCookTime());
        recipe.setServings(dto.getServings());
        recipe.setImageUrl(dto.getImageUrl());

        // Actualizar categorías
        Set<Category> newCats = dto.getCategories().stream()
                .map(catId -> categoryRepository.findById(catId)
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada")))
                .collect(Collectors.toSet());
        recipe.setCategories(newCats);

        // Ingredientes (reemplazar)
        recipe.getRecipeIngredients().clear();
        for (RecipeIngredientsDTO ing : dto.getIngredients()) {
            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(recipe);
            ri.setIngredient(ingredientRepository.findById(ing.getId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado")));
            ri.setUnit(unitRepository.findById(ing.getUnit_id())
                    .orElseThrow(() -> new RuntimeException("Unidad no encontrada")));
            ri.setQuantity(ing.getQuantity());
            recipe.getRecipeIngredients().add(ri);
        }

        // Pasos
        recipe.getSteps().clear();
        int index = 1;
        for (String instruction : dto.getSteps()) {
            RecipeStep step = new RecipeStep();
            step.setStep_number(index++);
            step.setInstruction(instruction);
            step.setRecipe(recipe);
            recipe.getSteps().add(step);
        }

        Recipe saved = recipeRepository.save(recipe);
        return convertToDTO(saved);
    }

    public RecipeDTO convertToDTO(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getRecipeId());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setCookTime(recipe.getCookTime());
        dto.setServings(recipe.getServings());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setAuthorEmail(recipe.getAuthor().getEmail());
        dto.setAuthorUsername(recipe.getAuthor().getUsername());


        // Categorías
        List<String> categoryNames = recipe.getCategories()
                .stream()
                .map(Category::getName)
                .toList();
        dto.setCategoryNames(categoryNames);

        // Ingredientes
        List<RecipeIngredientDTO> ingredients = recipe.getRecipeIngredients()
                .stream()
                .map(ri -> new RecipeIngredientDTO(
                        ri.getIngredient().getName(),
                        ri.getQuantity(),
                        ri.getUnit().getUnit_name()
                ))
                .toList();
        dto.setIngredients(ingredients);

        // Pasos
        List<String> steps = recipe.getSteps()
                .stream()
                .sorted(Comparator.comparing(RecipeStep::getStep_number)) // Asegura orden
                .map(RecipeStep::getInstruction)
                .toList();
        dto.setSteps(steps);

        return dto;
    }

    public List<RecipeSummaryDTO> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();

        return recipes.stream().map(r -> new RecipeSummaryDTO(
                r.getRecipeId(),
                r.getTitle(),
                r.getImageUrl(),
                r.getDescription().length() > 100
                        ? r.getDescription().substring(0, 100) + "..."
                        : r.getDescription()
        )).toList();
    }
}
