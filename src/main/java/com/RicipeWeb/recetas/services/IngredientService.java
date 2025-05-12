package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.IngredientDTO;
import com.RicipeWeb.recetas.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientDTO> getAllIngredients() {
        return ingredientRepository.findAllByOrderByNameAsc()
                .stream()
                .map(ingredient -> new IngredientDTO(ingredient.getIngredientId(), ingredient.getName()))
                .toList();
    }
}