package com.RicipeWeb.recetas.repositories;

import com.RicipeWeb.recetas.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long> {
}