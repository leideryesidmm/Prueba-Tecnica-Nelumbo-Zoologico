package com.nelumbo.apizoologico.services.dto.res;

import lombok.Data;

@Data
public class UsersDtoRes {
    private Long id;
    private String name;
    private String userEmail;
    private UsersDtoRes jefe;
    private String role;
}
