package com.RicipeWeb.recetas.repositories;

import com.RicipeWeb.recetas.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // Puedes agregar métodos personalizados si necesitas
}