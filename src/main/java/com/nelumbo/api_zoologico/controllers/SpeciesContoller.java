package com.nelumbo.api_zoologico.controllers;

import com.nelumbo.api_zoologico.services.SpeciesService;
import com.nelumbo.api_zoologico.services.dto.req.SpeciesDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.SpeciesDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/species")
@RequiredArgsConstructor
public class SpeciesContoller {
    private final SpeciesService speciesService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpeciesDtoRes> createSpecies(@Valid @RequestBody SpeciesDtoReq speciesDtoReq) {
        SpeciesDtoRes response = speciesService.createSpecies(speciesDtoReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpeciesDtoRes> updateSpecies(@PathVariable Long id, @Valid @RequestBody SpeciesDtoReq speciesDtoReq) {
        SpeciesDtoRes response = speciesService.updateSpecies(id, speciesDtoReq);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLEADO')")
    public ResponseEntity<SpeciesDtoRes> getSpeciesById(@PathVariable Long id) {
        SpeciesDtoRes response = speciesService.getSpeciesById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLEADO')")
    public ResponseEntity<List<SpeciesDtoRes>> getAllSpecies() {
        List<SpeciesDtoRes> response = speciesService.getAllSpecies();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSpecies(@PathVariable Long id) {
        speciesService.deleteSpecies(id);
        return ResponseEntity.noContent().build();
    }
}