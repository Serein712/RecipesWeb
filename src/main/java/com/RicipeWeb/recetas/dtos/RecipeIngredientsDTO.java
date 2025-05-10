package com.RicipeWeb.recetas.dtos;

import com.RicipeWeb.recetas.models.Unit;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientsDTO {

    private Long id;         // coincide con "id" en el JSON
    private Double quantity;
    private Long unit_id;
    private Unit unit;
}