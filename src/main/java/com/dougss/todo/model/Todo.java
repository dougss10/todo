package com.dougss.todo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Data
@NoArgsConstructor
@Audited
@Entity
public class Todo {

    public Todo(String description, User user) {
        this.description = description;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String description;
    private Boolean done = false;
    @NotNull
    @ManyToOne
    private User user;
}
