package com.dougss.todo.service;

import com.dougss.todo.dto.TodoDTO;
import com.dougss.todo.model.Todo;
import com.dougss.todo.model.User;
import com.dougss.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TodoService {

    final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public ArrayList<Todo> findAllByUser(User user) {
        ArrayList<Todo> todos = todoRepository.findAllByUser(user);
        return todos;
    }

    public Todo save(TodoDTO todoDTO, User user) {

        Todo todo = new Todo();

        if(todoDTO.getId() != null) {
            todo = getTodoById(todoDTO.getId());
        }

        todo.setDescription(todoDTO.getDescription());
        todo.setDone(todoDTO.getDone());
        todo.setUser(user);

        return todoRepository.save(todo);
    }

    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    public Todo getTodoById(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        return todo.orElse(null);
    }
}
