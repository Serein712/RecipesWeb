package com.RicipeWeb.recetas.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class RecipeDTO {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;

    private String authorUsername;
    private String authorEmail;

    private List<String> categoryNames;
    private List<RecipeIngredientDTO> ingredients;
    private List<String> steps;
}