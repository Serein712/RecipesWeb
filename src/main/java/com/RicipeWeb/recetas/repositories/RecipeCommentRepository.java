package com.RicipeWeb.recetas.repositories;

import com.RicipeWeb.recetas.models.Recipe;
import com.RicipeWeb.recetas.models.RecipeComment;
import com.RicipeWeb.recetas.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {

    Optional<RecipeComment> findByRecipeAndAuthor(Recipe recipe, User author);

    boolean existsByRecipeAndAuthor(Recipe recipe, User author);

    List<RecipeComment> findByRecipe(Recipe recipe);
    List<RecipeComment> findAllByOrderByCreatedAtDesc();

    void deleteAllByAuthor(User author);
    void deleteAllByRecipe(Recipe recipe);

    List<RecipeComment> findTop10ByRecipeAuthorOrderByCreatedAtDesc(User author);
}