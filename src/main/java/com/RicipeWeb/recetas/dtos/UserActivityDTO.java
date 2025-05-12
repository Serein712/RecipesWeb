package com.RicipeWeb.recetas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserActivityDTO {
    private String email;
    private long recipeCount;
}