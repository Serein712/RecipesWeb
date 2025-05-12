package com.RicipeWeb.recetas.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecipeCommentDTO {
    private Long id;
    private String authorName;
    private String authorEmail;
    private int rating;
    private String text;
    private LocalDateTime createdAt;
}