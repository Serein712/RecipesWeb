package com.RicipeWeb.recetas.dtos;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDTO {
    private String title;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String authorUsername;

    private List<Long> categoryIds;
    private List<RecipeIngredientsDTO> ingredients;
}