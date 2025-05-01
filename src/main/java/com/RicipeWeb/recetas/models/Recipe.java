package com.RicipeWeb.recetas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    private String title;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    // Getters y setters
}