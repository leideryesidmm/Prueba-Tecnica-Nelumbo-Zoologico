package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.SpeciesService;
import com.nelumbo.apizoologico.services.dto.req.SpeciesDtoReq;
import com.nelumbo.apizoologico.services.dto.res.SpeciesDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



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
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public ResponseEntity<SpeciesDtoRes> getSpeciesById(@PathVariable Long id) {
        SpeciesDtoRes response = speciesService.getSpeciesById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SpeciesDtoRes>> getAllSpecies(Pageable pageable) {
        Page<SpeciesDtoRes> response = speciesService.getAllSpecies(pageable);
        return ResponseEntity.ok(response);
    }
    @GetMapping(value = "user")
    @PreAuthorize("hasRole('JEFE') or hasRole('EMPLEADO')")
    public ResponseEntity<Page<SpeciesDtoRes>> getAllSpeciesByUser(Pageable pageable) {
        Page<SpeciesDtoRes> response = speciesService.getAllSpeciesByUser(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSpecies(@PathVariable Long id) {
        speciesService.deleteSpecies(id);
        return ResponseEntity.noContent().build();
    }
}