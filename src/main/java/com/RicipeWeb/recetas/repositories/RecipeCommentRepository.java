package com.RicipeWeb.recetas.repositories;

import com.RicipeWeb.recetas.models.Recipe;
import com.RicipeWeb.recetas.models.RecipeComment;
import com.RicipeWeb.recetas.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {
    List<RecipeComment> findByRecipe(Recipe recipe);
    Optional<RecipeComment> findByRecipeAndAuthor(Recipe recipe, User author);
    boolean existsByRecipeAndAuthor(Recipe recipe, User author);
}