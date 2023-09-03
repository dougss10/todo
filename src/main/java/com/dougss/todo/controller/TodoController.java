package com.dougss.todo.controller;

import com.dougss.todo.dto.TodoDTO;
import com.dougss.todo.dto.TodoOutputListDTO;
import com.dougss.todo.model.Todo;
import com.dougss.todo.model.User;
import com.dougss.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/todo", produces = {"application/json"})
@Tag(name = "todo")
public class TodoController {

    final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }


    @Operation(summary = "Search by id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search performed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal error"),
    })
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

    @Operation(summary = "Search Todo", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search performed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal error"),
    })
    @GetMapping
    public ResponseEntity<TodoOutputListDTO> get() {
        User user = getUserAuthenticated();
        ArrayList<Todo> todos = todoService.findAllByUser(user);
        TodoOutputListDTO todoOutputListDTO = new TodoOutputListDTO(todos);
        return ResponseEntity.ok(todoOutputListDTO);
    }

    @Operation(summary = "Save Todo", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Save performed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal error"),
    })@PostMapping
    public ResponseEntity<TodoDTO> post(@Valid @RequestBody TodoDTO todoDTO) {
        User user = getUserAuthenticated();
        Todo todo = todoService.save(todoDTO, user);
        TodoDTO todoOutputDTO = new TodoDTO(todo);
        return new ResponseEntity<>(todoOutputDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Update by id", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update performed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal error"),
    })
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

    @Operation(summary = "Delete by id", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete performed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal error"),
    })
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
