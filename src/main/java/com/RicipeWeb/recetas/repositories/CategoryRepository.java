package com.RicipeWeb.recetas.repositories;

import com.RicipeWeb.recetas.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}