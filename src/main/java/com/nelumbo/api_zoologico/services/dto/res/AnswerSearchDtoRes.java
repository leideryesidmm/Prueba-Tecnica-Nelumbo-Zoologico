package com.nelumbo.api_zoologico.services.dto.res;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerSearchDtoRes {
    private Long id;
    private UsersDtoRes user;
    private String menssage;
    private LocalDateTime date;
    private CommentDtoRes initialComment;
}
