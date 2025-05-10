package com.RicipeWeb.recetas.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSummaryDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private String shortDescription;
}