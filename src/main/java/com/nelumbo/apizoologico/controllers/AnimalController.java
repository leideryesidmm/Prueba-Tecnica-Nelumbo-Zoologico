package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.AnimalService;
import com.nelumbo.apizoologico.services.dto.req.AnimalDtoReq;
import com.nelumbo.apizoologico.services.dto.res.AnimalDtoRes;
import com.nelumbo.apizoologico.services.dto.res.AmountDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalService animalService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalDtoRes createAnimals(@Valid @RequestBody AnimalDtoReq animalDtoReq) {
        return animalService.createAnimal(animalDtoReq);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AnimalDtoRes updateAnimal(@PathVariable Long id, @Valid @RequestBody AnimalDtoReq animalDtoReq) {
        return animalService.updateAnimal(id, animalDtoReq);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public AnimalDtoRes getSpeciesById(@PathVariable Long id) {
        return animalService.getAnimalById(id);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AnimalDtoRes> getAllAnimals(Pageable pageable) {
        return animalService.getAllAnimals(pageable);
    }

    @GetMapping(value = "user")
    @PreAuthorize("hasRole('JEFE') or hasRole('EMPLEADO')")
    public Page<AnimalDtoRes> getAllAnimalsByUser(Pageable pageable) {
        return animalService.getAllAnimalsByUser(pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAnimals(@PathVariable Long id) {
        animalService.deleteAnimal(id);
    }
    @GetMapping("/count-by-zone/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AmountDtoRes countAnimalsByZone(@PathVariable Long id) {
        return new AmountDtoRes(animalService.countAnimalsByZone(id));
    }
    @GetMapping("/count-by-species/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AmountDtoRes countAnimalsBySpecies(@PathVariable Long id) {
        return new AmountDtoRes(animalService.countAnimalsBySpecies(id));
    }
    @GetMapping("/created-by-date/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AnimalDtoRes> findAllByDate(@PathVariable LocalDate date, Pageable pageable) {
        return animalService.findAllByDate(date,pageable);
    }
}