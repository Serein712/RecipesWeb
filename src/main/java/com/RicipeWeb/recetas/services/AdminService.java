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

        List<Recipe> recipes = recipeRepository.findByAuthor(user); // Todas sus recetas
        for (Recipe recipe : recipes) {
            commentRepository.deleteAllByRecipe(recipe); //Eliminar comentarios de esas recetas
        }

        recipeRepository.deleteAll(recipes); //Eliminar las recetas
        commentRepository.deleteAllByAuthor(user); //Eliminar sus comentarios

        userRepository.delete(user);
    }
}