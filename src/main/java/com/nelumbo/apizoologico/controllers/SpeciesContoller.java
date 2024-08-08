package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.SpeciesService;
import com.nelumbo.apizoologico.services.dto.req.SpeciesDtoReq;
import com.nelumbo.apizoologico.services.dto.res.SpeciesDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/species")
@RequiredArgsConstructor
public class SpeciesContoller {
    private final SpeciesService speciesService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public SpeciesDtoRes createSpecies(@Valid @RequestBody SpeciesDtoReq speciesDtoReq) {
        return speciesService.createSpecies(speciesDtoReq);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public SpeciesDtoRes updateSpecies(@PathVariable Long id, @Valid @RequestBody SpeciesDtoReq speciesDtoReq) {
        return speciesService.updateSpecies(id, speciesDtoReq);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public SpeciesDtoRes getSpeciesById(@PathVariable Long id) {
        return speciesService.getSpeciesById(id);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<SpeciesDtoRes> getAllSpecies(Pageable pageable) {
        return speciesService.getAllSpecies(pageable);
    }
    @GetMapping(value = "user")
    @PreAuthorize("hasRole('JEFE') or hasRole('EMPLEADO')")
    public Page<SpeciesDtoRes> getAllSpeciesByUser(Pageable pageable) {
        return speciesService.getAllSpeciesByUser(pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSpecies(@PathVariable Long id) {
        speciesService.deleteSpecies(id);
    }
}