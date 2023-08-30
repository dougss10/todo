package com.dougss.todo.controller;

import com.dougss.todo.dto.TodoDTO;
import com.dougss.todo.dto.TodoOutputListDTO;
import com.dougss.todo.model.Todo;
import com.dougss.todo.model.User;
import com.dougss.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/todo")
public class TodoController {

    final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getById(@PathVariable Long id) {
        User user = getUserAuthenticated();
        Todo todoTemp = todoService.getTodoById(id);
        if(todoTemp == null || !todoTemp.getUser().equals(user)) {
            return ResponseEntity.notFound().build();
        }
        TodoDTO todoDTO = new TodoDTO(todoTemp);
        return ResponseEntity.ok(todoDTO);
    }

    @GetMapping
    public ResponseEntity<TodoOutputListDTO> get() {
        User user = getUserAuthenticated();
        ArrayList<Todo> todos = todoService.findAllByUser(user);
        TodoOutputListDTO todoOutputListDTO = new TodoOutputListDTO(todos);
        return ResponseEntity.ok(todoOutputListDTO);
    }

    @PostMapping
    public ResponseEntity<TodoDTO> post(@Valid @RequestBody TodoDTO todoDTO) {
        User user = getUserAuthenticated();
        Todo todo = todoService.save(todoDTO, user);
        TodoDTO todoOutputDTO = new TodoDTO(todo);
        return new ResponseEntity<>(todoOutputDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDTO> put(@PathVariable Long id, @Valid @RequestBody TodoDTO todoDTO) {
        User user = getUserAuthenticated();
        Todo todoTemp = todoService.getTodoById(id);
        if(todoTemp == null || !todoTemp.getUser().equals(user)) {
            return ResponseEntity.notFound().build();
        }
        todoDTO.setId(id);
        Todo todo = todoService.save(todoDTO, user);
        TodoDTO todoOutputDTO = new TodoDTO(todo);
        return new ResponseEntity<>(todoOutputDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User user = getUserAuthenticated();
        Todo todoTemp = todoService.getTodoById(id);

        if(todoTemp == null || !todoTemp.getUser().equals(user)) {
            return ResponseEntity.notFound().build();
        }

        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private User getUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
