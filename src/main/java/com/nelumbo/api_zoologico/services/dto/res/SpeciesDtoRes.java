package com.nelumbo.api_zoologico.services.dto.res;

import lombok.Data;

@Data
public class SpeciesDtoRes {
    private Long id;
    private ZoneDtoRes zone;
    private String name;
}