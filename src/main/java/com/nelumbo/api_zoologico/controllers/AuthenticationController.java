package com.nelumbo.api_zoologico.controllers;

import com.nelumbo.api_zoologico.services.dto.res.AuthDtoRes;
import com.nelumbo.api_zoologico.services.AuthService;
import com.nelumbo.api_zoologico.services.dto.req.LoginDtoReq;
import com.nelumbo.api_zoologico.services.dto.req.RegisterDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.UsersDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthDtoRes> login(@Valid @RequestBody LoginDtoReq request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDtoRes> register(@Valid @RequestBody RegisterDtoReq request)
    {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping(value = "logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authService.logout(authHeader);
        }
        return ResponseEntity.ok().build();
    }
}