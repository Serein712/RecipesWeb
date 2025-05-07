package com.RicipeWeb.recetas.repositories;

import com.RicipeWeb.recetas.models.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}