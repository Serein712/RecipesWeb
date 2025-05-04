package com.RicipeWeb.recetas.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeDTO {
    private Long id;
    private String title;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String authorUsername;

    // Getters y setters
}