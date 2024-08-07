package com.nelumbo.apizoologico.services;

import com.nelumbo.apizoologico.exceptions.ConflictException;
import com.nelumbo.apizoologico.exceptions.InvalidRoleException;
import com.nelumbo.apizoologico.exceptions.ResourceNotFoundException;
import com.nelumbo.apizoologico.entities.*;
import com.nelumbo.apizoologico.repositories.AnimalRepository;
import com.nelumbo.apizoologico.repositories.SpeciesRepository;
import com.nelumbo.apizoologico.services.dto.req.AnimalDtoReq;
import com.nelumbo.apizoologico.services.dto.res.AnimalDtoRes;
import com.nelumbo.apizoologico.services.dto.res.UsersDtoRes;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalService {
    private final AnimalRepository repository;
    private final UsersService usersService;
    private final TokenService tokenService;
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
        if(id==null){
            throw new IllegalArgumentException("Error, el id de usuario no puede ser null");
        }
        Animal animal = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal con el id " + id + " no encontrado"));
        return this.modelMapper.map(animal, AnimalDtoRes.class);
    }

    @Transactional
    public void deleteAnimal(Long id) {
        Optional<Animal> animal=this.repository.findById(id);
        if (animal.isEmpty())
            throw new ResourceNotFoundException("Animal con id " + id + " no encontrado");
        this.repository.deleteById(id);
    }

    public Page<AnimalDtoRes> getAllAnimals(Pageable pageable) {
            Page<Animal> animals = this.repository.findAll(pageable);
            List<AnimalDtoRes> animalDtoRes =animals.stream()
                    .map(animal -> modelMapper.map(animal, AnimalDtoRes.class))
                    .toList();
            return new PageImpl<>(animalDtoRes,pageable,animals.getTotalElements());
    }
    public Page<AnimalDtoRes> getAllAnimalsByUser(Pageable pageable) {
            UsersDtoRes user=usersService.getUserById(tokenService.getUserId());
            Page<Animal> animals;
            Users user1=new Users();
            if(user.getRole().equalsIgnoreCase(Role.JEFE.toString())){
                user1.setId(user.getId());
            }
            else if(user.getRole().equalsIgnoreCase(Role.EMPLEADO.toString())){
                user1.setId(user.getJefe().getId());
            }
            else {
                throw  new InvalidRoleException("Rol no valido");
            }
            animals=this.repository.findAllBySpeciesZoneJefe(user1, pageable);
            List<AnimalDtoRes> animalDtoRes= animals.getContent().stream()
                    .map(animal -> modelMapper.map(animal, AnimalDtoRes.class))
                    .toList();
        return new PageImpl<>(animalDtoRes,pageable,animals.getTotalElements());
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

    public Page<AnimalDtoRes> findAllByDate(LocalDate date, Pageable pageable) {
            Page<Animal> animals = this.repository.findAllByCreatedDate(date,pageable);
            List<AnimalDtoRes> animalDtoRes=animals.getContent().stream()
                    .map(animal -> modelMapper.map(animal, AnimalDtoRes.class))
                    .toList();
            return new PageImpl<>(animalDtoRes,pageable,animals.getTotalElements());
    }

    public Page<AnimalDtoRes> searchAnimalsByWord(String word,Pageable pageable) {
        UsersDtoRes user=usersService.getUserById(tokenService.getUserId());
        Users user1=new Users();
        Page<Animal> animals;
        if(user.getRole().equalsIgnoreCase(Role.JEFE.toString())){
            user1.setId(user.getId());
            animals=this.repository.findByNameContainingIgnoreCaseAndSpeciesZoneJefe(word,user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.EMPLEADO.toString())){
            user1.setId(user.getJefe().getId());
            animals=this.repository.findByNameContainingIgnoreCaseAndSpeciesZoneJefe(word,user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.ADMIN.toString())){
            animals=this.repository.findByNameContainingIgnoreCase(word,pageable);
        }
        else {
            throw new InvalidRoleException("Rol de usuario invalido");
        }
        List<AnimalDtoRes> animalDtoRes= animals
                .stream()
                .map(animal -> modelMapper.map(animal, AnimalDtoRes.class))
                .toList();
        return new PageImpl<>(animalDtoRes,pageable,animals.getTotalElements());
    }
}
