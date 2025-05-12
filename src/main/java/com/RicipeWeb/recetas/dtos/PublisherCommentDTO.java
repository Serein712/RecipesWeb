package com.RicipeWeb.recetas.dtos;

import com.RicipeWeb.recetas.models.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherCommentDTO {
    private String recipeTitle;
    private Long recipeId;
    private String commentText;
    private int rating;
    private String commenterName;
    private LocalDateTime date;
    private Recipe recipe;


    public PublisherCommentDTO(String title, Long recipeId, String text, int rating, String username) {
        this.recipeTitle = title;
        this.recipeId = recipeId;
        this.commentText = text;
        this.rating = rating;
        this.commenterName = username;
        this.date = LocalDateTime.now();
    }
}