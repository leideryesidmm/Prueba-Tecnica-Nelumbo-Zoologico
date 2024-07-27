package com.nelumbo.api_zoologico.services;

import com.nelumbo.api_zoologico.Exceptions.ConflictException;
import com.nelumbo.api_zoologico.Exceptions.ResourceNotFoundException;
import com.nelumbo.api_zoologico.entities.Species;
import com.nelumbo.api_zoologico.entities.Zone;
import com.nelumbo.api_zoologico.repositories.SpeciesRepository;
import com.nelumbo.api_zoologico.services.dto.req.SpeciesDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.SpeciesDtoRes;
import com.nelumbo.api_zoologico.services.dto.res.ZoneDtoRes;
import lombok.RequiredArgsConstructor;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpeciesService {
    private final SpeciesRepository repository;
    private final AnimalService animalService;
    private final ZoneService zoneService;
    private final ModelMapper modelMapper;
    public SpeciesDtoRes createSpecies(SpeciesDtoReq speciesDtoReq) {
        if (this.repository.existsByNameIgnoreCase(speciesDtoReq.getName())) {
            throw new ConflictException("La especie "+speciesDtoReq.getName()+" ya existe.");
        }
        ZoneDtoRes zone=zoneService.getZoneById(speciesDtoReq.getZone());
        Species species = this.modelMapper.map(speciesDtoReq, Species.class);
        species.setZone(this.modelMapper.map(zone,Zone.class));
        Species savedSpecies = this.repository.save(species);

        return this.modelMapper.map(savedSpecies, SpeciesDtoRes.class);
    }

    public SpeciesDtoRes updateSpecies(Long id, SpeciesDtoReq speciesDtoReq) {
        Species species = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especie con id " + id + " no encontarada"));

        if (this.repository.existsByNameIgnoreCase(speciesDtoReq.getName()) && !species.getName().equalsIgnoreCase(speciesDtoReq.getName())) {
            throw new ConflictException("La especie "+speciesDtoReq.getName()+" ya existe.");
        }

        this.modelMapper.map(speciesDtoReq, species);
        Species updatedSpecies = this.repository.save(species);

        return this.modelMapper.map(updatedSpecies, SpeciesDtoRes.class);
    }


    public SpeciesDtoRes getSpeciesById(Long id) {
        Species species = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especie con el id " + id + " no encontrada"));
        return this.modelMapper.map(species, SpeciesDtoRes.class);
    }

    @Transactional
    public void deleteSpecies(Long id) {
        Species species = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especie con id " + id + " no encontrada"));
        long animalCount = animalService.countAnimalsBySpecies(species.getId());

        if (animalCount > 0) {
            throw new ConflictException("No se puede eliminar una especie con animales asociados");
        }

        this.repository.deleteById(id);
    }

    public List<SpeciesDtoRes> getAllSpecies() {
        try {
            List<Species> species = this.repository.findAll();
            return species.stream()
                    .map(species1 -> modelMapper.map(species1, SpeciesDtoRes.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }

    public List<SpeciesDtoRes> searchSpeciesByWord(String word) {
        return this.repository.findByNameContainingIgnoreCase(word)
                .stream()
                .map(species -> modelMapper.map(species, SpeciesDtoRes.class))
                .collect(Collectors.toList());
    }
}
