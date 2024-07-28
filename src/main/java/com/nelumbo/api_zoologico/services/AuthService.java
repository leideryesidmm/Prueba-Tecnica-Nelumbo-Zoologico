package com.nelumbo.api_zoologico.services;

import com.nelumbo.api_zoologico.entities.Role;
import com.nelumbo.api_zoologico.entities.Users;
import com.nelumbo.api_zoologico.security.jwt.JwtService;
import com.nelumbo.api_zoologico.services.dto.req.LoginDtoReq;
import com.nelumbo.api_zoologico.services.dto.req.RegisterDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.AuthDtoRes;
import com.nelumbo.api_zoologico.services.dto.res.UsersDtoRes;
import lombok.RequiredArgsConstructor;
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

    public AuthDtoRes login(LoginDtoReq request) {
        System.out.println(request.getUserEmail()+request.getPass());
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

    public UsersDtoRes register(RegisterDtoReq request) {
        Users user = Users.builder()
                .userEmail(request.getUserEmail())
                .pass(passwordEncoder.encode( request.getPass()))
                .name(request.getName())
                .role(Role.EMPLEADO)
                .build();
        return usersService.createUser(user);
    }

    public void logout(String authHeader) {
        String token = authHeader.substring(7);
        jwtService.revokeToken(token);
    }
}
