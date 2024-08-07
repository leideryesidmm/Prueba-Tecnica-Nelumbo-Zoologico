package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.AnimalService;
import com.nelumbo.apizoologico.services.dto.req.AnimalDtoReq;
import com.nelumbo.apizoologico.services.dto.res.AnimalDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/animals")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public ResponseEntity<AnimalDtoRes> getSpeciesById(@PathVariable Long id) {
        AnimalDtoRes response = animalService.getAnimalById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AnimalDtoRes>> getAllAnimals(Pageable pageable) {
        Page<AnimalDtoRes> response = animalService.getAllAnimals(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "user")
    @PreAuthorize("hasRole('JEFE') or hasRole('EMPLEADO')")
    public ResponseEntity<Page<AnimalDtoRes>> getAllAnimalsByUser(Pageable pageable) {
        Page<AnimalDtoRes> response = animalService.getAllAnimalsByUser(pageable);
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
    public ResponseEntity<Map<String, Long>> countAnimalsByZone(@PathVariable Long id) {
        return ResponseEntity.ok(Collections.singletonMap("cantAnimals",animalService.countAnimalsByZone(id)));
    }
    @GetMapping("/count-by-species/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> countAnimalsBySpecies(@PathVariable Long id) {
        return ResponseEntity.ok(Collections.singletonMap("cantAnimals",animalService.countAnimalsBySpecies(id)));
    }
    @GetMapping("/created-by-date/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AnimalDtoRes>> findAllByDate(@PathVariable LocalDate date, Pageable pageable) {
        return ResponseEntity.ok(animalService.findAllByDate(date,pageable));
    }
}
