package com.RicipeWeb.recetas.repositories;

import com.RicipeWeb.recetas.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAllByOrderByNameAsc();
}