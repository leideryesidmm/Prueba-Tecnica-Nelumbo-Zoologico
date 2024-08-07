package com.nelumbo.apizoologico.services;

import com.nelumbo.apizoologico.exceptions.ConflictException;
import com.nelumbo.apizoologico.entities.Role;
import com.nelumbo.apizoologico.entities.Users;
import com.nelumbo.apizoologico.security.jwt.JwtService;
import com.nelumbo.apizoologico.services.dto.req.LoginDtoReq;
import com.nelumbo.apizoologico.services.dto.req.RegisterDtoReq;
import com.nelumbo.apizoologico.services.dto.res.AuthDtoRes;
import com.nelumbo.apizoologico.services.dto.res.UsersDtoRes;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersService usersService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    public AuthDtoRes login(LoginDtoReq request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserEmail(), request.getPass()));
        UserDetails user=usersService.findByUserEmail(request.getUserEmail());
        Users userEntity = (Users) user;

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", userEntity.getId());

        String token=jwtService.getToken(extraClaims,user);
        return AuthDtoRes.builder()
                .token(token)
                .build();

    }

    public UsersDtoRes registerEmployee(RegisterDtoReq request) {
        UsersDtoRes jefe=usersService.getUserById(request.getJefe());
        if (!jefe.getRole().equalsIgnoreCase(Role.JEFE.toString())){
            throw new ConflictException("Solo los jefes pueden tener empleados a cargo.");
        }
        Users user = Users.builder()
                .userEmail(request.getUserEmail())
                .pass(passwordEncoder.encode( request.getPass()))
                .name(request.getName())
                .jefe(this.modelMapper.map(jefe,Users.class))
                .role(Role.EMPLEADO)
                .build();
        return usersService.createUser(user);
    }
    public UsersDtoRes registerJefe(RegisterDtoReq request) {
        Users user = Users.builder()
                .userEmail(request.getUserEmail())
                .pass(passwordEncoder.encode( request.getPass()))
                .name(request.getName())
                .role(Role.JEFE)
                .build();
        return usersService.createUser(user);
    }

    public void logout(String authHeader) {
        String token = authHeader.substring(7);
        jwtService.revokeToken(token);
    }
}
