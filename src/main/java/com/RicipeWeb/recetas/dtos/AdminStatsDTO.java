package com.RicipeWeb.recetas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class AdminStatsDTO {
    private long totalUsers;
    private long totalRecipes;
    private long totalComments;
    private List<UserActivityDTO> topAuthors;
    private Map<String, Long> registrationsByMonth;
}