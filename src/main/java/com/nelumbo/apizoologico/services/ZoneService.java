package com.nelumbo.apizoologico.services;

import com.nelumbo.apizoologico.exceptions.ConflictException;
import com.nelumbo.apizoologico.exceptions.InvalidRoleException;
import com.nelumbo.apizoologico.exceptions.ResourceNotFoundException;
import com.nelumbo.apizoologico.entities.Role;
import com.nelumbo.apizoologico.entities.Users;
import com.nelumbo.apizoologico.entities.Zone;
import com.nelumbo.apizoologico.repositories.ZoneRepository;
import com.nelumbo.apizoologico.services.dto.req.ZonaDtoReq;
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
public class ZoneService {
    private final ZoneRepository repository;
    private final AnimalService animalService;
    private final UsersService usersService;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;
    public ZoneDtoRes createZone(ZonaDtoReq zonaDtoReq) {
        if (this.repository.existsByNameIgnoreCase(zonaDtoReq.getName())) {
            throw new ConflictException("La zona "+zonaDtoReq.getName()+" ya existe.");
        }
        UsersDtoRes user=this.usersService.getUserById(zonaDtoReq.getJefe());
        if(!user.getRole().equalsIgnoreCase(Role.JEFE.toString())){
            throw new ConflictException("Solo los jefes pueden tener zonas asignadas.");
        }
        Zone zone = this.modelMapper.map(zonaDtoReq, Zone.class);
        zone.setJefe(this.modelMapper.map(user,Users.class));
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
        if(id==null){
            throw new IllegalArgumentException("Error, el id de zone no puede ser null");
        }
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

    public Page<ZoneDtoRes> getAllZone(Pageable pageable) {
        Page<Zone> zones= this.repository.findAll(pageable);
        List<ZoneDtoRes> zoneDtoRes=zones.stream()
                .map(zone -> modelMapper.map(zone, ZoneDtoRes.class))
                .toList();
        return new PageImpl<>(zoneDtoRes,pageable,zones.getTotalElements());
    }
    public Page<ZoneDtoRes> getAllZoneByUser(Pageable pageable) {
            UsersDtoRes user=usersService.getUserById(tokenService.getUserId());
            Page<Zone> zones;
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
            zones=this.repository.findAllByJefe(user1,pageable);
            List<ZoneDtoRes> zoneDtoRes=zones.stream()
                    .map(zone -> modelMapper.map(zone, ZoneDtoRes.class))
                    .toList();
            return new PageImpl<>(zoneDtoRes,pageable,zones.getTotalElements());
    }
    public Page<ZoneDtoRes> searchZonesByWord(String word, Pageable pageable){
        UsersDtoRes user=usersService.getUserById(tokenService.getUserId());
        Users user1=new Users();
        Page<Zone> zones;
        if(user.getRole().equalsIgnoreCase(Role.JEFE.toString())){
            user1.setId(user.getId());
            zones=this.repository.findByNameContainingIgnoreCaseAndJefe(word,user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.EMPLEADO.toString())){
            user1.setId(user.getJefe().getId());
            zones=this.repository.findByNameContainingIgnoreCaseAndJefe(word,user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.ADMIN.toString())){
            zones=this.repository.findByNameContainingIgnoreCase(word,pageable);
        }
        else {
            throw new InvalidRoleException("Rol de usuario invalido");
        }
        List<ZoneDtoRes> zoneDtoRes= zones
                .getContent().stream()
                .map(zone -> modelMapper.map(zone, ZoneDtoRes.class))
                .toList();
        return new PageImpl<>(zoneDtoRes, pageable, zones.getTotalElements());
    }
}
