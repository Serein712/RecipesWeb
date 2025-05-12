package com.RicipeWeb.recetas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAdminDTO {
    private Long id;
    private String email;
    private String role;
    private int recipeCount;
}