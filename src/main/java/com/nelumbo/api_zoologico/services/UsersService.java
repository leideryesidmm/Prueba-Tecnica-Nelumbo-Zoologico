package com.nelumbo.api_zoologico.services;

import com.nelumbo.api_zoologico.Exceptions.ConflictException;
import com.nelumbo.api_zoologico.Exceptions.ResourceNotFoundException;
import com.nelumbo.api_zoologico.entities.Users;
import com.nelumbo.api_zoologico.repositories.UsersRepository;
import com.nelumbo.api_zoologico.services.dto.req.UsersDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.UsersDtoRes;
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
public class UsersService {
    private final UsersRepository repository;
    private final ModelMapper modelMapper;
    public UsersDtoRes createUser(Users user) {
        if(this.repository.existsByUserEmail(user.getUserEmail().toLowerCase()))
            throw  new ConflictException("Ya existe un usuario con el email"+user.getUserEmail());
        Users createdUser=this.repository.save(user);
        return this.modelMapper.map(createdUser, UsersDtoRes.class);
    }

    public UsersDtoRes updateUser(Long id, UsersDtoReq usersDtoReq) {
        if (usersDtoReq.getUserEmail() != null) {
            usersDtoReq.setUserEmail(usersDtoReq.getUserEmail().toLowerCase());
        }
        Users user = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con id " + id + " no encontarado"));

        if (this.repository.existsByUserEmail(usersDtoReq.getUserEmail().toLowerCase())
                && !user.getUserEmail().equalsIgnoreCase(usersDtoReq.getUserEmail())) {
            throw new ConflictException("El usuario con email "+usersDtoReq.getUserEmail()+" ya existe.");
        }

        if (usersDtoReq.getName() != null) {
            user.setName(usersDtoReq.getName());
        }

        Users updatedUser = this.repository.save(user);

        return this.modelMapper.map(updatedUser, UsersDtoRes.class);
    }


    public UsersDtoRes getUserById(Long id) {
        if(id==null){
            throw new IllegalArgumentException("Error, el id de usuario no puede ser null");
        }
        Users user= this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con el id " + id + " no encontrado"));
        return this.modelMapper.map(user, UsersDtoRes.class);
    }
    @Transactional
    public void disableUser(Long Id) {
        Users user = this.repository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con id " + Id + " no encontrado"));
        this.repository.disableUser(Id,true);
    }
    @Transactional
    public void enableUser(Long Id) {
        Users user = this.repository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con id " + Id + " no encontrado"));
        this.repository.disableUser(Id,false);
    }

    public List<UsersDtoRes> getAllUsers() {
        try {
            List<Users> users = this.repository.findAll();
            return users.stream()
                    .map(user -> modelMapper.map(user, UsersDtoRes.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }

    public Users findByUserEmail(String userEmail) {
        return this.repository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con email" + userEmail + " no encontarado"));
    }

    public boolean existsByUserEmail(String userEmail) {
        return this.repository.existsByUserEmail(userEmail);
    }
    public List<UsersDtoRes> getDisabledUsers() {
        try{
        List<Users> users = this.repository.findByDisable(true);
        return users.stream()
                .map(user -> modelMapper.map(user, UsersDtoRes.class))
                .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }
    public List<UsersDtoRes> getEnabledUsers() {
        try{
        List<Users> users = this.repository.findByDisable(false);
        return users.stream()
                .map(user -> modelMapper.map(user, UsersDtoRes.class))
                .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }

    public void deleteUser(Long id) {
        Users species = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con id " + id + " no encontrado"));
        this.repository.deleteById(id);
    }
}