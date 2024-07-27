package com.nelumbo.api_zoologico.services.dto.res;

import lombok.Data;

import java.util.List;

@Data
public class CommentAnswerDtoRes {
    private CommentDtoRes commentDtoRes;
    private List<AnswerDtoRes> answerDtoRes;
}
