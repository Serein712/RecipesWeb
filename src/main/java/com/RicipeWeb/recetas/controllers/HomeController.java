package com.RicipeWeb.recetas.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "¡Bienvenido a la Plataforma de Recetas!";
    }
}
