package com.nelumbo.apizoologico.services.mappers;

import com.nelumbo.apizoologico.entities.Role;
import com.nelumbo.apizoologico.entities.Users;
import com.nelumbo.apizoologico.services.dto.res.UsersDtoRes;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UsersToUsersDtoRes implements IMapper<Users, UsersDtoRes> {
    @Override
    public UsersDtoRes map(Users in) {
        UsersDtoRes usersDtoRes=new UsersDtoRes();
        usersDtoRes.setId(in.getId());
        usersDtoRes.setUserEmail(in.getUserEmail());
        usersDtoRes.setName(in.getName());
        usersDtoRes.setRole(in.getRole().toString());
        if(!Objects.equals(in.getJefe(),null))
            usersDtoRes.setJefe(map(in.getJefe()));
        else usersDtoRes.setJefe(null);
        return usersDtoRes;
    }
    @Override
    public Users map2(UsersDtoRes in) {
        Users user=new Users();
        user.setId(in.getId());
        user.setUserEmail(in.getUserEmail());
        user.setName(in.getName());
        user.setRole(Role.valueOf(in.getRole()));
        if(!Objects.equals(in.getJefe(),null))
            user.setJefe(map2(in.getJefe()));
        else user.setJefe(null);
        return user;
    }
}