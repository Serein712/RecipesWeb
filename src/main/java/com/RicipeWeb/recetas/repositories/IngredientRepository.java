package com.RicipeWeb.recetas.repositories;
import com.RicipeWeb.recetas.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}