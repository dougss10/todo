package com.dougss.todo.dto;

import com.dougss.todo.model.Todo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TodoDTO {

    public TodoDTO(Todo todo) {
        this.id = todo.getId();
        this.description = todo.getDescription();
        this.done = todo.getDone();
    }

    private Long id;
    @NotBlank(message="description must have value.")
    private String description;
    private Boolean done = false;
}
