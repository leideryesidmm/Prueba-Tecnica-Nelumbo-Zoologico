package com.nelumbo.apizoologico.services;

import com.nelumbo.apizoologico.services.dto.res.SearchDtoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ZoneService zoneService;
    private final SpeciesService speciesService;
    private final AnimalService animalService;
    private final CommentService commentService;

    public SearchDtoRes searchByWord(String word, Pageable pageable){
        SearchDtoRes searchDtoRes=new SearchDtoRes();
        searchDtoRes.setZones(zoneService.searchZonesByWord(word,pageable));
        searchDtoRes.setSpecies(speciesService.searchSpeciesByWord(word,pageable));
        searchDtoRes.setAnimals(animalService.searchAnimalsByWord(word,pageable));
        searchDtoRes.setComments(commentService.searchCommentsByWord(word,pageable));
        searchDtoRes.setAnswers(commentService.searchAnswersByWord(word,pageable));
        return searchDtoRes;
    }
}
