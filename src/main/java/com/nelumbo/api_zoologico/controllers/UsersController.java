package com.nelumbo.api_zoologico.controllers;

import com.nelumbo.api_zoologico.services.UsersService;
import com.nelumbo.api_zoologico.services.dto.req.UsersDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.AnimalDtoRes;
import com.nelumbo.api_zoologico.services.dto.res.UsersDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDtoRes> updateUser(@PathVariable Long id, @Valid @RequestBody UsersDtoReq usersDtoReq) {
        UsersDtoRes response = this.usersService.updateUser(id, usersDtoReq);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDtoRes> getUserById(@PathVariable Long id) {
        UsersDtoRes response = this.usersService.getUserById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsersDtoRes>> getAllUsers() {
        List<UsersDtoRes> response = this.usersService.getAllUsers();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/disabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsersDtoRes>> getAllUsersDisable() {
        List<UsersDtoRes> response = this.usersService.getDisabledUsers();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsersDtoRes>> getAllUsersEnable() {
        List<UsersDtoRes> response = this.usersService.getEnabledUsers();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        usersService.disableUser(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableUser(@PathVariable Long id) {
        usersService.enableUser(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }}