package com.RicipeWeb.recetas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recipe_steps")
public class RecipeStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;

    private Integer stepNumber;

    @Column(nullable = false)
    private String instruction;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}