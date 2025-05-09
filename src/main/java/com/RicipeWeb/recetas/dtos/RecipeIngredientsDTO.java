package com.RicipeWeb.recetas.dtos;

import lombok.*;

@Getter
@Setter
public class RecipeIngredientsDTO {
    //private Long ingredientId;
    //private double quantity;
    //private Long unitId;
    private Long id;         // coincide con "id" en el JSON
    private Double quantity;
    private Long unit_id;

}