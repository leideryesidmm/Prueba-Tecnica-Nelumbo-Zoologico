package com.nelumbo.apizoologico.services.dto.res;

import lombok.Data;

import java.util.List;

@Data
public class CommentAnswerDtoRes {
    private CommentDtoRes comment;
    private List<AnswerDtoRes> answers;
}
