package com.nelumbo.api_zoologico.controllers;

import com.nelumbo.api_zoologico.services.CommentService;
import com.nelumbo.api_zoologico.services.dto.req.CommentDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.CommentAnswerDtoRes;
import com.nelumbo.api_zoologico.services.dto.res.CommentDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CommentDtoRes> createCommentOrAnswer(@Valid @RequestBody CommentDtoReq commentDtoReq) {
        CommentDtoRes response = commentService.createComment(commentDtoReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/answers/{idAnimal}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<CommentAnswerDtoRes>> getAllCommentsByAnimal(@PathVariable Long idAnimal) {
        List<CommentAnswerDtoRes> response = commentService.getAllCommentAnswers(idAnimal);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/avg-answer-comment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getAvg() {
        Double response = commentService.getAvg();
        return ResponseEntity.ok(response);
    }

}
