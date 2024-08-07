package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.dto.res.AuthDtoRes;
import com.nelumbo.apizoologico.services.AuthService;
import com.nelumbo.apizoologico.services.dto.req.LoginDtoReq;
import com.nelumbo.apizoologico.services.dto.req.RegisterDtoReq;
import com.nelumbo.apizoologico.services.dto.res.UsersDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

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

    @PostMapping(value = "register/employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDtoRes> registerEmployee(@Valid @RequestBody RegisterDtoReq request)
    {
        return ResponseEntity.ok(authService.registerEmployee(request));
    }
    @PostMapping(value = "register/boss")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDtoRes> registerBoss(@Valid @RequestBody RegisterDtoReq request)
    {
        return ResponseEntity.ok(authService.registerJefe(request));
    }
    @PostMapping(value = "logout")
    public ResponseEntity<Map<String,String>> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authService.logout(authHeader);
        }
        return ResponseEntity.ok(Collections.singletonMap("message","Logout"));
    }
}