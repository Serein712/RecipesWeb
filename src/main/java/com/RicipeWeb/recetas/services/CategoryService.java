package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.CategoryDTO;
import com.RicipeWeb.recetas.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(c -> new CategoryDTO(c.getCategory_id(), c.getName()))
                .collect(Collectors.toList());
    }
}