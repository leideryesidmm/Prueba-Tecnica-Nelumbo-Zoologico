package com.nelumbo.api_zoologico.services.dto.res;

import lombok.Data;

import java.util.List;
@Data
public class SearchDtoRes {
    private List<ZoneDtoRes> zones;
    private List<AnimalDtoRes> animals;
    private List<SpeciesDtoRes> species;
    private List<CommentDtoRes> comments;
}
