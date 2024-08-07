package com.nelumbo.apizoologico.services;

import com.nelumbo.apizoologico.exceptions.ConflictException;
import com.nelumbo.apizoologico.exceptions.ResourceNotFoundException;
import com.nelumbo.apizoologico.entities.Users;
import com.nelumbo.apizoologico.repositories.UsersRepository;
import com.nelumbo.apizoologico.services.dto.req.UsersDtoReq;
import com.nelumbo.apizoologico.services.dto.res.UsersDtoRes;
import com.nelumbo.apizoologico.services.mappers.UsersToUsersDtoRes;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository repository;
    private final ModelMapper modelMapper;
    private final UsersToUsersDtoRes mapper;
    private String userNotFound="Usuario con el id {0} no encontrado.";
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
                .orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(userNotFound,id)));

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
                .orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(userNotFound,id)));
        return this.modelMapper.map(user, UsersDtoRes.class);
    }
    @Transactional
    public void disableUser(Long id) {
        Optional<Users> user = this.repository.findById(id);
        if(user.isEmpty())
            throw new ResourceNotFoundException(MessageFormat.format(userNotFound,id));
        this.repository.disableUser(id,true);
    }
    @Transactional
    public void enableUser(Long id) {
        Optional<Users> user = this.repository.findById(id);
        if(user.isEmpty())
            throw new ResourceNotFoundException(MessageFormat.format(userNotFound,id));
        this.repository.disableUser(id,false);
    }

    public List<UsersDtoRes> getAllUsers() {
            List<Users> users = this.repository.findAll();
            return users.stream()
                    .map(user->this.modelMapper.map(user, UsersDtoRes.class))
                    .toList();
    }

    public Users findByUserEmail(String userEmail) {
        return this.repository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con email" + userEmail + " no encontarado"));
    }

    public boolean existsByUserEmail(String userEmail) {
        return this.repository.existsByUserEmail(userEmail);
    }
    public List<UsersDtoRes> getDisabledUsers() {
        List<Users> users = this.repository.findByDisable(true);
        return users.stream()
                .map(user -> modelMapper.map(user, UsersDtoRes.class))
                .toList();
    }
    public List<UsersDtoRes> getEnabledUsers() {
        List<Users> users = this.repository.findByDisable(false);
        return users.stream()
                .map(user -> modelMapper.map(user, UsersDtoRes.class))
                .toList();
    }

    public void deleteUser(Long id) {
        Optional<Users> user = this.repository.findById(id);
        if(user.isEmpty())
                throw new ResourceNotFoundException(MessageFormat.format(userNotFound,id));
        this.repository.deleteById(id);
    }
}