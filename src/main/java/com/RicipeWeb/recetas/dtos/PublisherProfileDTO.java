package com.RicipeWeb.recetas.dtos;

import com.RicipeWeb.recetas.models.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PublisherProfileDTO {
    private String name;
    private String email;
    private String bio;
    private double averageRating;
    private List<RecipeSummaryDTO> recipes;
    private List<PublisherCommentDTO> recentComments;


}