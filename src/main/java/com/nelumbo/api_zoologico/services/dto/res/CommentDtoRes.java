package com.nelumbo.api_zoologico.services.dto.res;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDtoRes {
    private Long id;
    private AnimalDtoRes animal;
    private UsersDtoRes user;
    private LocalDateTime date;
    private String menssage;
}
