package com.nelumbo.apizoologico.services;

import com.nelumbo.apizoologico.exceptions.ConflictException;
import com.nelumbo.apizoologico.exceptions.InvalidCommentException;
import com.nelumbo.apizoologico.exceptions.InvalidRoleException;
import com.nelumbo.apizoologico.exceptions.ResourceNotFoundException;
import com.nelumbo.apizoologico.entities.*;
import com.nelumbo.apizoologico.repositories.CommentRepository;
import com.nelumbo.apizoologico.services.dto.req.CommentDtoReq;
import com.nelumbo.apizoologico.services.dto.res.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final AnimalService animalService;
    private final UsersService usersService;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;

    public CommentDtoRes createComment(CommentDtoReq commentDtoReq) {
        AnimalDtoRes animal=animalService.getAnimalById(commentDtoReq.getAnimal());
        Long userid=tokenService.getUserId();
        UsersDtoRes user=this.usersService.getUserById(userid);
        if(user.getRole().equalsIgnoreCase(Role.JEFE.toString())&&!animal.getSpecies().getZone().getJefe().getId().equals(user.getId())){
                throw new ConflictException("No puede añadir comentarios a animales de zonas no asignadas.");
        }
        if(user.getRole().equalsIgnoreCase(Role.EMPLEADO.toString())&&!animal.getSpecies().getZone().getJefe().getId().equals(user.getJefe().getId())){
                throw new ConflictException("No puede añadir comentarios a animales de zonas no asignadas.");
        }
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
            List<Comment> comments = this.repository.findByInitialCommentIsNullAndAnimal(animal);
            return comments.stream()
                    .map(comment -> modelMapper.map(comment, CommentDtoRes.class))
                    .toList();
    }
    public List<AnswerDtoRes> getAllAnswers(Long idInitialComment) {
        Comment initialComment = this.repository.findById(idInitialComment)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario con el id " + idInitialComment + " no encontrado"));

        if (initialComment.getInitialComment() != null) {
            throw new InvalidCommentException("El id " + initialComment.getId() + " no es un comentario inicial, es una respuesta");
        }

            List<Comment> comments = this.repository.findAllByInitialComment(initialComment);

            return comments.stream()
                    .map(comment -> modelMapper.map(comment, AnswerDtoRes.class))
                    .toList();
    }

    public Page<CommentAnswerDtoRes> getAllCommentAnswers(Long idAnimal, Pageable pageable) {
            List<CommentDtoRes> commentDtoResList = getAllComments(idAnimal);
            List<CommentAnswerDtoRes> commentAnswerDtoResList = new ArrayList<>();

            for (CommentDtoRes commentDto : commentDtoResList) {
                CommentAnswerDtoRes commentAnswerDtoRes = new CommentAnswerDtoRes();
                commentAnswerDtoRes.setComment(commentDto);
                commentAnswerDtoRes.setAnswers(getAllAnswers(commentDto.getId()));
                commentAnswerDtoResList.add(commentAnswerDtoRes);
            }

            return new PageImpl<>(commentAnswerDtoResList,pageable,commentAnswerDtoResList.size());
    }
    public List<CommentAnswerDtoRes> getAllCommentAnswersByComment(List<CommentDtoRes> commentDtoResList) {
            List<CommentAnswerDtoRes> commentAnswerDtoResList = new ArrayList<>();

            for (CommentDtoRes commentDto : commentDtoResList) {
                CommentAnswerDtoRes commentAnswerDtoRes = new CommentAnswerDtoRes();
                commentAnswerDtoRes.setComment(commentDto);
                commentAnswerDtoRes.setAnswers(getAllAnswers(commentDto.getId()));
                commentAnswerDtoResList.add(commentAnswerDtoRes);
            }

            return commentAnswerDtoResList;
    }


    public Double getPercentage() {
            Long comments=this.repository.countByInitialComment(null);
            Long answered=this.repository.countCommentsWithResponses();
            return (double) answered / comments * 100;
    }

    public Page<CommentAnswerDtoRes> searchCommentsByWord(String word,Pageable pageable) {
        UsersDtoRes user=usersService.getUserById(tokenService.getUserId());
        Users user1=new Users();
        Page<Comment> comments;
        if(user.getRole().equalsIgnoreCase(Role.JEFE.toString())){
            user1.setId(user.getId());
            comments=this.repository.findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNullAndAnimalSpeciesZoneJefe(word,user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.EMPLEADO.toString())){
            user1.setId(user.getJefe().getId());
            comments=this.repository.findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNullAndAnimalSpeciesZoneJefe(word, user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.ADMIN.toString())){
            comments=this.repository.findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNull(word,pageable);
        }
        else {
            throw new InvalidRoleException("Rol de usuario invalido");
        }
        List<CommentDtoRes> commentDtoRes=comments
                .getContent().stream()
                .map(comment -> modelMapper.map(comment, CommentDtoRes.class))
                .toList();
        return new PageImpl<>(getAllCommentAnswersByComment(commentDtoRes),pageable,comments.getTotalElements());
    }

    public Page<AnswerSearchDtoRes> searchAnswersByWord(String word, Pageable pageable) {
        UsersDtoRes user=usersService.getUserById(tokenService.getUserId());
        Users user1=new Users();
        Page<Comment> comments;
        if(user.getRole().equalsIgnoreCase(Role.JEFE.toString())){
            user1.setId(user.getId());
            comments=this.repository.findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNotNullAndAnimalSpeciesZoneJefe(word,user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.EMPLEADO.toString())){
            user1.setId(user.getJefe().getId());
            comments=this.repository.findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNotNullAndAnimalSpeciesZoneJefe(word, user1,pageable);
        }
        else if(user.getRole().equalsIgnoreCase(Role.ADMIN.toString())){
            comments=this.repository.findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNotNull(word, pageable);
        }
        else {
            throw new InvalidRoleException("Rol de usuario invalido");
        }

        List<AnswerSearchDtoRes> answerSearchDtoRes= comments.stream()
                .map(comment -> new ModelMapper().map(comment, AnswerSearchDtoRes.class)).toList();
        return new PageImpl<>(answerSearchDtoRes,pageable,comments.getTotalElements());
    }
}
