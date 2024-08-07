package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.SearchService;
import com.nelumbo.apizoologico.services.dto.res.SearchDtoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/searchs")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public ResponseEntity<SearchDtoRes> search(@RequestParam("word") String word, Pageable pageable) {
        if (Objects.isNull(word) || word.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        SearchDtoRes results = searchService.searchByWord(word, pageable);
        return ResponseEntity.ok(results);
    }
}
