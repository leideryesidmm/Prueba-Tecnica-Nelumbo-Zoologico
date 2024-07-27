package com.nelumbo.api_zoologico.controllers;

import com.nelumbo.api_zoologico.services.SearchService;
import com.nelumbo.api_zoologico.services.dto.res.AnimalDtoRes;
import com.nelumbo.api_zoologico.services.dto.res.SearchDtoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<SearchDtoRes> search(@RequestParam("word") String word) {
        if (Objects.isNull(word) || word.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        SearchDtoRes results = searchService.searchByWord(word);
        return ResponseEntity.ok(results);
    }
}
