package com.RicipeWeb.recetas.repositories;

import com.RicipeWeb.recetas.models.Recipe;
import com.RicipeWeb.recetas.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByAuthor(User author);
    List<Recipe> findByTitleContainingIgnoreCase(String title);
    void deleteAllByAuthor(User author);
    int countByAuthor(User user);
}