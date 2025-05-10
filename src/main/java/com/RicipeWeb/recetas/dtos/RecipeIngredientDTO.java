package com.RicipeWeb.recetas.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientDTO {
    private String ingredient;
    private Double quantity;
    private String unit;
}
