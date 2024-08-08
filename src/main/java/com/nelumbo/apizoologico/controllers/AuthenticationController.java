package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.dto.res.AuthDtoRes;
import com.nelumbo.apizoologico.services.AuthService;
import com.nelumbo.apizoologico.services.dto.req.LoginDtoReq;
import com.nelumbo.apizoologico.services.dto.req.RegisterDtoReq;
import com.nelumbo.apizoologico.services.dto.res.MessageDtoRes;
import com.nelumbo.apizoologico.services.dto.res.UsersDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping(value = "login")
    public AuthDtoRes login(@Valid @RequestBody LoginDtoReq request)
    {
        return authService.login(request);
    }

    @PostMapping(value = "register/employee")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UsersDtoRes registerEmployee(@Valid @RequestBody RegisterDtoReq request)
    {
        return authService.registerEmployee(request);
    }
    @PostMapping(value = "register/boss")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UsersDtoRes registerBoss(@Valid @RequestBody RegisterDtoReq request)
    {
        return authService.registerJefe(request);
    }
    @PostMapping(value = "logout")
    public MessageDtoRes logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authService.logout(authHeader);
        }
        return new MessageDtoRes("Logout");
    }
}