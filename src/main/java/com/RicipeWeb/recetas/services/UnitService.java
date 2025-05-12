package com.RicipeWeb.recetas.services;

import com.RicipeWeb.recetas.dtos.UnitDTO;
import com.RicipeWeb.recetas.repositories.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public List<UnitDTO> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(unit -> new UnitDTO(unit.getUnit_id(), unit.getUnit_name()))
                .collect(Collectors.toList());
    }
}