package com.nelumbo.api_zoologico.services;

import com.nelumbo.api_zoologico.Exceptions.InvalidCommentException;
import com.nelumbo.api_zoologico.Exceptions.ResourceNotFoundException;
import com.nelumbo.api_zoologico.entities.*;
import com.nelumbo.api_zoologico.repositories.CommentRepository;
import com.nelumbo.api_zoologico.services.dto.req.CommentDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final AnimalService animalService;
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    public CommentDtoRes createComment(CommentDtoReq commentDtoReq) {
        AnimalDtoRes animal=animalService.getAnimalById(commentDtoReq.getAnimal());
        UsersDtoRes user=usersService.getUserById(commentDtoReq.getUser());

        Comment comment=new Comment();
        comment.setAnimal(this.modelMapper.map(animal,Animal.class));
        comment.setUser(this.modelMapper.map(user,Users.class));
        comment.setDate(LocalDateTime.now());
        comment.setMenssage(commentDtoReq.getMenssage());

        if (commentDtoReq.getInitialComment() != null) {
            Comment initialComment=this.repository.findById(commentDtoReq.getInitialComment())
                    .orElseThrow(() -> new ResourceNotFoundException("Comentario con el id " + commentDtoReq.getInitialComment() + " no encontrado"));
            if(initialComment.getInitialComment()==null){
                comment.setInitialComment(initialComment);
            }else {
                throw new InvalidCommentException("El id "+initialComment.getId()+"no es un comentario, es una respuesta");
            }
        }
        Comment savedComment=this.repository.save(comment);

        return this.modelMapper.map(savedComment, CommentDtoRes.class);
    }


    public List<CommentDtoRes> getAllComments(Long idAnimal) {
            AnimalDtoRes animalDtoRes=animalService.getAnimalById(idAnimal);
            Animal animal=this.modelMapper.map(animalDtoRes,Animal.class);
        try {
            List<Comment> comments = this.repository.findByInitialCommentIsNullAndAnimal(animal);
            return comments.stream()
                    .map(comment -> modelMapper.map(comment, CommentDtoRes.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }
    public List<AnswerDtoRes> getAllAnswers(Long idInitialComment) {
        Comment initialComment = this.repository.findById(idInitialComment)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario con el id " + idInitialComment + " no encontrado"));

        if (initialComment.getInitialComment() != null) {
            throw new InvalidCommentException("El id " + initialComment.getId() + " no es un comentario inicial, es una respuesta");
        }

        try {
            List<Comment> comments = this.repository.findAllByInitialComment(initialComment);

            return comments.stream()
                    .map(comment -> modelMapper.map(comment, AnswerDtoRes.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }

    public List<CommentAnswerDtoRes> getAllCommentAnswers(Long idAnimal) {
        try {
            List<CommentDtoRes> commentDtoResList = getAllComments(idAnimal);
            List<CommentAnswerDtoRes> commentAnswerDtoResList = new ArrayList<>();

            for (CommentDtoRes commentDto : commentDtoResList) {
                CommentAnswerDtoRes commentAnswerDtoRes = new CommentAnswerDtoRes();
                commentAnswerDtoRes.setCommentDtoRes(commentDto);
                commentAnswerDtoRes.setAnswerDtoRes(getAllAnswers(commentDto.getId()));
                commentAnswerDtoResList.add(commentAnswerDtoRes);
            }

            return commentAnswerDtoResList;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        } catch (MappingException e) {
            throw new RuntimeException("Error al mapear los datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }

    public Double getAvg() {
        try{
            Long comments=this.repository.countByInitialComment(null);
            Long answered=this.repository.countCommentsWithResponses();
            return (double) answered / comments * 100;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<CommentDtoRes> searchCommentsByWord(String word) {
        return this.repository.findByMenssageContainingIgnoreCase(word)
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDtoRes.class))
                .collect(Collectors.toList());
    }
}
