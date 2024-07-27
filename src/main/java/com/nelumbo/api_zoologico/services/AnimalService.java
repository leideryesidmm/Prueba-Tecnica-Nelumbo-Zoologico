package com.nelumbo.api_zoologico.services;

import com.nelumbo.api_zoologico.Exceptions.ConflictException;
import com.nelumbo.api_zoologico.Exceptions.ResourceNotFoundException;
import com.nelumbo.api_zoologico.entities.Animal;
import com.nelumbo.api_zoologico.entities.Species;
import com.nelumbo.api_zoologico.entities.Zone;
import com.nelumbo.api_zoologico.repositories.AnimalRepository;
import com.nelumbo.api_zoologico.repositories.SpeciesRepository;
import com.nelumbo.api_zoologico.services.dto.req.AnimalDtoReq;
import com.nelumbo.api_zoologico.services.dto.req.SpeciesDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.AnimalDtoRes;
import com.nelumbo.api_zoologico.services.dto.res.SpeciesDtoRes;
import com.nelumbo.api_zoologico.services.dto.res.ZoneDtoRes;
import lombok.RequiredArgsConstructor;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalService {
    private final AnimalRepository repository;
    private final ModelMapper modelMapper;
    private final SpeciesRepository speciesRepository;
    public AnimalDtoRes createAnimal(AnimalDtoReq animalDtoReq) {
        if (this.repository.existsByNameIgnoreCase(animalDtoReq.getName())) {
            throw new ConflictException("El animal "+animalDtoReq.getName()+" ya existe.");
        }
        Species species=speciesRepository.findById(animalDtoReq.getSpecies())
                .orElseThrow(() -> new ResourceNotFoundException("Especie con el id " + animalDtoReq.getSpecies() + " no encontrada"));

        Animal animal = this.modelMapper.map(animalDtoReq, Animal.class);
        animal.setSpecies(species);
        animal.setCreateDate(LocalDateTime.now());
        Animal savedAnimal = this.repository.save(animal);


        return this.modelMapper.map(savedAnimal, AnimalDtoRes.class);
    }

    public AnimalDtoRes updateAnimal(Long id, AnimalDtoReq animalDtoReq) {
        Animal animal = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal con id " + id + " no encontarado"));

        if (this.repository.existsByNameIgnoreCase(animalDtoReq.getName()) && !animal.getName().equals(animalDtoReq.getName())) {
            throw new ConflictException("El animal "+animalDtoReq.getName()+" ya existe.");
        }

        this.modelMapper.map(animalDtoReq, animal);
        Animal updatedAnimal = this.repository.save(animal);

        return this.modelMapper.map(updatedAnimal, AnimalDtoRes.class);
    }

    public AnimalDtoRes getAnimalById(Long id) {
        Animal animal = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal con el id " + id + " no encontrado"));
        return this.modelMapper.map(animal, AnimalDtoRes.class);
    }

    @Transactional
    public void deleteAnimal(Long Id) {
        Animal animal = this.repository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal con id " + Id + " no encontrado"));
        this.repository.deleteById(Id);
    }

    public List<AnimalDtoRes> getAllAnimals() {
        try {
            List<Animal> animals = this.repository.findAll();
            return animals.stream()
                    .map(animal -> modelMapper.map(animal, AnimalDtoRes.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }
    public long countAnimalsByZone(Long idZone) {
        Zone zone=new Zone();
        zone.setId(idZone);
        return this.repository.countBySpeciesZone(zone);
    }
    public long countAnimalsBySpecies(Long idSpecies) {
        Species species=new Species();
        species.setId(idSpecies);
        return this.repository.countBySpecies(species);}

    public List<AnimalDtoRes> findAllByDate(LocalDate date) {
        try {
            List<Animal> animals = this.repository.findAllByCreatedDate(date);
            return animals.stream()
                    .map(animal -> modelMapper.map(animal, AnimalDtoRes.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new RuntimeException("Error: se ha encontrado un valor nulo inesperado: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }

    public List<AnimalDtoRes> searchAnimalsByWord(String word) {
        return this.repository.findByNameContainingIgnoreCase(word)
                .stream()
                .map(animal -> modelMapper.map(animal, AnimalDtoRes.class))
                .collect(Collectors.toList());
    }
}
