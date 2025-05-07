package com.RicipeWeb.recetas.dtos;

import lombok.*;

@Getter
@Setter
public class RecipeIngredientsDTO {
    private Long ingredientId;
    private double quantity;
    private Long unitId;

}