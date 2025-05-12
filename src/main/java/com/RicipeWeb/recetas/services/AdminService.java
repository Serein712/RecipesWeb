package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.models.Recipe;
import com.RicipeWeb.recetas.models.User;
import com.RicipeWeb.recetas.repositories.RecipeCommentRepository;
import com.RicipeWeb.recetas.repositories.RecipeRepository;
import com.RicipeWeb.recetas.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeCommentRepository commentRepository;

    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 1. Obtener todas las recetas del usuario
        List<Recipe> recipes = recipeRepository.findByAuthor(user);

        // 2. Eliminar todos los comentarios de esas recetas
        for (Recipe recipe : recipes) {
            commentRepository.deleteAllByRecipe(recipe);
        }

        // 3. Eliminar las recetas
        recipeRepository.deleteAll(recipes);

        // 4. Eliminar los comentarios escritos por el usuario
        commentRepository.deleteAllByAuthor(user);

        // 5. Finalmente eliminar al usuario
        userRepository.delete(user);
    }
}