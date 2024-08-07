package com.nelumbo.apizoologico.services.dto.res;

import lombok.Data;

@Data
public class ZoneDtoRes {
    private Long id;
    private String name;
    private UsersDtoRes jefe;
}
