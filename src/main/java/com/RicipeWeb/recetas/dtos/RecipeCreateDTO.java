package com.RicipeWeb.recetas.dtos;

import java.util.List;

public class RecipeCreateDTO {
    public String title;
    public String description;
    public Integer prepTime;
    public Integer cookTime;
    public Integer servings;
    public List<IngredientInput> ingredients;  // mínimo 1
    public List<String> steps;                 // mínimo 1
    public List<Long> categories;              // IDs de categorías
    public String imageUrl;                    // opcional

    public static class IngredientInput {
        public Long ingredientId;
        public Double quantity;
        public Long unitId;
    }
}
