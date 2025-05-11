package com.RicipeWeb.recetas.dtos;

import lombok.Data;

@Data
public class RecipeCommentCreateDTO {
    private int rating;
    private String text;
}