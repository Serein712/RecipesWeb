package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.RecipeDTO;
import com.RicipeWeb.recetas.models.Recipe;
import com.RicipeWeb.recetas.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .collect(Collectors.toList());
    }

    public RecipeDTO getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .orElse(null);
    }
}