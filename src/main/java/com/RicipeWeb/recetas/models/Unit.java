package com.RicipeWeb.recetas.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "units")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unit_id;

    private String unit_name;
}