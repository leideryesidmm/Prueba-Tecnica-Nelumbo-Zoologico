package com.nelumbo.apizoologico.services;

import com.nelumbo.apizoologico.exceptions.ConflictException;
import com.nelumbo.apizoologico.exceptions.InvalidRoleException;
import com.nelumbo.apizoologico.exceptions.ResourceNotFoundException;
import com.nelumbo.apizoologico.entities.Role;
import com.nelumbo.apizoologico.entities.Species;
import com.nelumbo.apizoologico.entities.Users;
import com.nelumbo.apizoologico.entities.Zone;
import com.nelumbo.apizoologico.repositories.SpeciesRepository;
import com.nelumbo.apizoologico.services.dto.req.SpeciesDtoReq;
import com.nelumbo.apizoologico.services.dto.res.SpeciesDtoRes;
import com.nelumbo.apizoologico.services.dto.res.UsersDtoRes;
import com.nelumbo.apizoologico.services.dto.res.ZoneDtoRes;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SpeciesService {
    private final SpeciesRepository repository;
    private final AnimalService animalService;
    private final ZoneService zoneService;
    private final UsersService usersService;
    private final TokenService tokenService;
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
        if(id==null){
            throw new IllegalArgumentException("Error, el id de usuario no puede ser null");
        }
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

    public Page<SpeciesDtoRes> getAllSpecies(Pageable pageable) {
            Page<Species> species = this.repository.findAll(pageable);
            List<SpeciesDtoRes> speciesDtoRes= species.stream()
                    .map(species1 -> modelMapper.map(species1, SpeciesDtoRes.class))
                    .toList();
            return new PageImpl<>(speciesDtoRes,pageable,species.getTotalElements());
    }

    public Page<SpeciesDtoRes> getAllSpeciesByUser(Pageable pageable) {
            UsersDtoRes user=usersService.getUserById(tokenService.getUserId());
            Page<Species> species;
            Users user1=new Users();
            if(user.getRole().equalsIgnoreCase(Role.JEFE.toString())){
                user1.setId(user.getId());
            }
            else if(user.getRole().equalsIgnoreCase(Role.EMPLEADO.toString())){
                user1.setId(user.getJefe().getId());
            }else {
                throw  new InvalidRoleException("Rol no valido");
            }
            species=this.repository.findAllByZoneJefe(user1,pageable);

            List<SpeciesDtoRes> speciesDtoRes= species.getContent().stream()
                    .map(species1 -> modelMapper.map(species1, SpeciesDtoRes.class))
                    .toList();
            return new PageImpl<>(speciesDtoRes,pageable,species.getTotalElements());
    }

    public Page<SpeciesDtoRes> searchSpeciesByWord(String word, Pageable pageable) {
        UsersDtoRes user=usersService.getUserById(tokenService.getUserId());
        Users user1=new Users();
        Page<Species> species;
        if(user.getRole().equalsIgnoreCase(Role.JEFE.toString())){
            user1.setId(user.getId());
            species=this.repository.findByNameContainingIgnoreCaseAndZoneJefe(word,user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.EMPLEADO.toString())){
            user1.setId(user.getJefe().getId());
            species=this.repository.findByNameContainingIgnoreCaseAndZoneJefe(word,user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.ADMIN.toString())){
            species=this.repository.findByNameContainingIgnoreCase(word,pageable);
        }else {
            throw new InvalidRoleException("Rol de usuario invalido");
        }
        List<SpeciesDtoRes> speciesDtoRes=  species
                .stream()
                .map(species1 -> modelMapper.map(species1, SpeciesDtoRes.class))
                .toList();
        return new PageImpl<>(speciesDtoRes,pageable,species.getTotalElements());
    }
}
