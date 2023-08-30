package com.dougss.todo.dto;

import com.dougss.todo.model.Todo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class TodoOutputListDTO {

    public TodoOutputListDTO(ArrayList<Todo> todos) {
        todos.forEach(todo -> this.todos.add(new TodoDTO(todo)));
    }

    private ArrayList<TodoDTO> todos = new ArrayList<>();
}
