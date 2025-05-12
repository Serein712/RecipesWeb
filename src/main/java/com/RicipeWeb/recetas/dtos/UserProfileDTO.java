package com.RicipeWeb.recetas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String email;
    private String role;
    private String name;
    //private LocalDateTime createdAt;
}