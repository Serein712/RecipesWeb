package com.RicipeWeb.recetas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    private String mediaUrl;

    @Column(nullable = false)
    private String mediaType;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}