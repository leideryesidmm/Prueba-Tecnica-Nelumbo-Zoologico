package com.nelumbo.api_zoologico.services;

import com.nelumbo.api_zoologico.services.dto.res.SearchDtoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ZoneService zoneService;
    private final SpeciesService speciesService;
    private final AnimalService animalService;
    private final CommentService commentService;

    public SearchDtoRes searchByWord(String word){
        SearchDtoRes searchDtoRes=new SearchDtoRes();
        searchDtoRes.setZones(zoneService.searchZonesByWord(word));
        searchDtoRes.setSpecies(speciesService.searchSpeciesByWord(word));
        searchDtoRes.setAnimals(animalService.searchAnimalsByWord(word));
        searchDtoRes.setComments(commentService.searchCommentsByWord(word));
        return null;
    }
}
