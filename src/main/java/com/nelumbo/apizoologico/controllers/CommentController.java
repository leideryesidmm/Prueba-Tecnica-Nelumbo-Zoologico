package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.CommentService;
import com.nelumbo.apizoologico.services.dto.req.CommentDtoReq;
import com.nelumbo.apizoologico.services.dto.res.CommentAnswerDtoRes;
import com.nelumbo.apizoologico.services.dto.res.CommentDtoRes;
import com.nelumbo.apizoologico.services.dto.res.PercentageDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoRes createCommentOrAnswer(@Valid @RequestBody CommentDtoReq commentDtoReq) {
        return commentService.createComment(commentDtoReq);
    }
    @GetMapping("/answers/{idAnimal}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public Page<CommentAnswerDtoRes> getAllCommentsByAnimal(@PathVariable Long idAnimal, Pageable pageable) {
        return commentService.getAllCommentAnswers(idAnimal,pageable);
    }
    @GetMapping("/percentage-answer-comment")
    @PreAuthorize("hasRole('ADMIN')")
    public PercentageDtoRes getPercentage() {
        return new PercentageDtoRes(commentService.getPercentage());
    }

}
