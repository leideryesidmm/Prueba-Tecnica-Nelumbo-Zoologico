package com.nelumbo.apizoologico.services.dto.res;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimalDtoRes {
    private Long id;
    private SpeciesDtoRes species;
    private String name;
    private LocalDateTime createDate;

}
