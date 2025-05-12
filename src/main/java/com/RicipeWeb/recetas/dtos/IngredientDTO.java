package com.RicipeWeb.recetas.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientDTO {
    private Long id;
    private String name;

    public IngredientDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}