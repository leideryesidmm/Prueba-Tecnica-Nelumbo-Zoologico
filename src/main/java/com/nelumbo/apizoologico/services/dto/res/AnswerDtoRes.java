package com.nelumbo.apizoologico.services.dto.res;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerDtoRes {
    private Long id;
    private UsersDtoRes user;
    private String menssage;
    private LocalDateTime date;
}
