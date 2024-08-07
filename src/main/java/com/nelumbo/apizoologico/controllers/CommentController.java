package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.CommentService;
import com.nelumbo.apizoologico.services.dto.req.CommentDtoReq;
import com.nelumbo.apizoologico.services.dto.res.CommentAnswerDtoRes;
import com.nelumbo.apizoologico.services.dto.res.CommentDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public ResponseEntity<CommentDtoRes> createCommentOrAnswer(@Valid @RequestBody CommentDtoReq commentDtoReq) {
        CommentDtoRes response = commentService.createComment(commentDtoReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/answers/{idAnimal}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public ResponseEntity<Page<CommentAnswerDtoRes>> getAllCommentsByAnimal(@PathVariable Long idAnimal, Pageable pageable) {
        Page<CommentAnswerDtoRes> response = commentService.getAllCommentAnswers(idAnimal,pageable);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/percentage-answer-comment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Double>> getPercentage() {
        Double response = commentService.getPercentage();
        return ResponseEntity.ok(Collections.singletonMap("percentage",response));
    }

}
