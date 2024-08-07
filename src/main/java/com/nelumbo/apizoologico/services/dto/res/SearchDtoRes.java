package com.nelumbo.apizoologico.services.dto.res;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class SearchDtoRes {
    private Page<ZoneDtoRes> zones;
    private Page<AnimalDtoRes> animals;
    private Page<SpeciesDtoRes> species;
    private Page<CommentAnswerDtoRes> comments;
    private Page<AnswerSearchDtoRes> answers;

}
