package com.nelumbo.apizoologico.exceptions;

import com.nelumbo.apizoologico.services.dto.res.MessageDtoRes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDtoRes handleIllegalArgumentException(IllegalArgumentException e) {
        return new MessageDtoRes(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<MessageDtoRes> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<MessageDtoRes> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> errors.add(new MessageDtoRes(error.getDefaultMessage())));
        return errors;
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageDtoRes handleResourceNotFoundException(ResourceNotFoundException e) {
        return new MessageDtoRes(e.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageDtoRes handleRuntimeException(RuntimeException e) {
        return new MessageDtoRes("Internal server error: " + e.getMessage());
    }
    @ExceptionHandler(ConflictException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public MessageDtoRes handleConflictException(ConflictException e) {
        return new MessageDtoRes(e.getMessage());
    }
    @ExceptionHandler(InvalidCommentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDtoRes handleInvalidCommentException(InvalidCommentException e) {
        return new MessageDtoRes(e.getMessage());
    }
    @ExceptionHandler(InvalidRoleException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public MessageDtoRes handleInvalidRoleException(InvalidRoleException e) {
        return new MessageDtoRes(e.getMessage());
    }
}