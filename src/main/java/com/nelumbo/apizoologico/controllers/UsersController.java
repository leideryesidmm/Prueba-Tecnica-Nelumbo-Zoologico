package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.UsersService;
import com.nelumbo.apizoologico.services.dto.req.UsersDtoReq;
import com.nelumbo.apizoologico.services.dto.res.UsersDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsersDtoRes updateUser(@PathVariable Long id, @Valid @RequestBody UsersDtoReq usersDtoReq) {
        return this.usersService.updateUser(id, usersDtoReq);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsersDtoRes getUserById(@PathVariable Long id) {
        return this.usersService.getUserById(id);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsersDtoRes> getAllUsers() {
        return this.usersService.getAllUsers();
    }
    @GetMapping("/disabled")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsersDtoRes> getAllUsersDisable() {
        return this.usersService.getDisabledUsers();
    }
    @GetMapping("/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsersDtoRes> getAllUsersEnable() {
        return this.usersService.getEnabledUsers();
    }
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableUser(@PathVariable Long id) {
        usersService.disableUser(id);
    }
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableUser(@PathVariable Long id) {
        usersService.enableUser(id);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
    }}