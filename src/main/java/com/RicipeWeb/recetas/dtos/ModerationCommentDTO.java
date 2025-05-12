package com.RicipeWeb.recetas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ModerationCommentDTO {
    private Long commentId;
    private String recipeTitle;
    private String authorEmail;
    private int rating;
    private String text;
    private LocalDateTime createdAt;
}