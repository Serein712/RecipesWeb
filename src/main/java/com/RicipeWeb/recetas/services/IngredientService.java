package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.IngredientDTO;
import com.RicipeWeb.recetas.models.Ingredient;
import com.RicipeWeb.recetas.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientDTO> getAllIngredients() {
        return ingredientRepository.findAll()
                .stream()
                .map(ingredient -> new IngredientDTO(ingredient.getIngredientId(), ingredient.getName()))
                .toList();
    }
}