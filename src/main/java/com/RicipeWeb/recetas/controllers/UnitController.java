package com.RicipeWeb.recetas.controllers;

import com.RicipeWeb.recetas.dtos.UnitDTO;
import com.RicipeWeb.recetas.services.UnitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    public List<UnitDTO> getAllUnits() {
        return unitService.getAllUnits();
    }
}