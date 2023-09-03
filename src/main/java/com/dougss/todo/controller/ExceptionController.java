package com.dougss.todo.controller;

import com.dougss.todo.dto.ErrorMessageDTO;
import com.dougss.todo.exception.RegisterException;
import com.dougss.todo.exception.TodoManipulationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController  {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> TodoManipulationExceptionHandler(ConstraintViolationException exception, WebRequest request) {

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = constraintViolations.stream()
                .map(constraintViolation -> String.format("%s value: ('%s'),  %s", constraintViolation.getPropertyPath(),
                        constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Object> TodoManipulationExceptionHandler(LockedException exception, WebRequest request) {
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(exception.getMessage());
        return new ResponseEntity<>(errorMessageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> TodoManipulationExceptionHandler(BadCredentialsException exception, WebRequest request) {
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(exception.getMessage());
        return new ResponseEntity<>(errorMessageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TodoManipulationException.class)
    public ResponseEntity<Object> TodoManipulationExceptionHandler(TodoManipulationException exception, WebRequest request) {
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(exception.getMessage());
        return new ResponseEntity<>(errorMessageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<Object> TodoManipulationExceptionHandler(RegisterException exception, WebRequest request) {
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(exception.getMessage());
        return new ResponseEntity<>(errorMessageDTO, HttpStatus.BAD_REQUEST);
    }
}
