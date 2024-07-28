package com.nelumbo.api_zoologico.controllers;

import com.nelumbo.api_zoologico.services.AnimalService;
import com.nelumbo.api_zoologico.services.dto.req.AnimalDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.AnimalDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/animal")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalService animalService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnimalDtoRes> createAnimals(@Valid @RequestBody AnimalDtoReq animalDtoReq) {
        AnimalDtoRes response = animalService.createAnimal(animalDtoReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnimalDtoRes> updateAnimal(@PathVariable Long id, @Valid @RequestBody AnimalDtoReq animalDtoReq) {
        AnimalDtoRes response = animalService.updateAnimal(id, animalDtoReq);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLEADO')")
    public ResponseEntity<AnimalDtoRes> getSpeciesById(@PathVariable Long id) {
        AnimalDtoRes response = animalService.getAnimalById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLEADO')")
    public ResponseEntity<List<AnimalDtoRes>> getAllAnimals() {
        List<AnimalDtoRes> response = animalService.getAllAnimals();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnimals(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/count-by-zone/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countAnimalsByZone(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.countAnimalsByZone(id));
    }
    @GetMapping("/count-by-species/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countAnimalsBySpecies(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.countAnimalsBySpecies(id));
    }
    @GetMapping("/created-by-date/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AnimalDtoRes>> findAllByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(animalService.findAllByDate(date));
    }
}
