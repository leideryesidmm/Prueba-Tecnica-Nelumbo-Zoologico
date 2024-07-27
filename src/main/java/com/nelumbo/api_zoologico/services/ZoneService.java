package com.nelumbo.api_zoologico.services;

import com.nelumbo.api_zoologico.Exceptions.ConflictException;
import com.nelumbo.api_zoologico.Exceptions.ResourceNotFoundException;
import com.nelumbo.api_zoologico.entities.Zone;
import com.nelumbo.api_zoologico.repositories.ZoneRepository;
import com.nelumbo.api_zoologico.services.dto.req.ZonaDtoReq;
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
public class ZoneService {
    private final ZoneRepository repository;
    private final AnimalService animalService;

    private final ModelMapper modelMapper;
    public ZoneDtoRes createZone(ZonaDtoReq zonaDtoReq) {
        if (this.repository.existsByNameIgnoreCase(zonaDtoReq.getName())) {
            throw new ConflictException("La zona "+zonaDtoReq.getName()+" ya existe.");
        }

        Zone zone = this.modelMapper.map(zonaDtoReq, Zone.class);
        Zone savedZone = this.repository.save(zone);

        return this.modelMapper.map(savedZone, ZoneDtoRes.class);
    }

    public ZoneDtoRes updateZone(Long id, ZonaDtoReq zonaDtoReq) {
        Zone zone = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zona con id " + id + " no encontarada"));

        if (this.repository.existsByNameIgnoreCase(zonaDtoReq.getName()) && !zone.getName().equalsIgnoreCase(zonaDtoReq.getName())) {
            throw new ConflictException("La zona "+zonaDtoReq.getName()+" ya existe.");
        }

        this.modelMapper.map(zonaDtoReq, zone);
        Zone updatedZone = this.repository.save(zone);

        return this.modelMapper.map(updatedZone, ZoneDtoRes.class);
    }


    public ZoneDtoRes getZoneById(Long id) {
        Zone zone = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zona con el id " + id + " no encontrada"));
        return this.modelMapper.map(zone, ZoneDtoRes.class);
    }

    @Transactional
    public void deleteZone(Long zoneId) {
        Zone zone = this.repository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zona con id " + zoneId + " no encontrada"));
        long animalCount = animalService.countAnimalsByZone(zone.getId());

        if (animalCount > 0) {
            throw new ConflictException("No se puede eliminar una zona con animales asociados");
        }

        this.repository.deleteById(zoneId);
    }

    public List<ZoneDtoRes> getAllZone() {
        try{
        List<Zone> zones= this.repository.findAll();
        return zones.stream()
                .map(zone -> modelMapper.map(zone, ZoneDtoRes.class))
                .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }
    public List<ZoneDtoRes> searchZonesByWord(String word){
        return this.repository.findByNameContainingIgnoreCase(word)
                .stream()
                .map(zone -> modelMapper.map(zone, ZoneDtoRes.class))
                .collect(Collectors.toList());
    }
}
