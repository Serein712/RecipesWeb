package com.RicipeWeb.recetas.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long category_id;
    private String name;
}