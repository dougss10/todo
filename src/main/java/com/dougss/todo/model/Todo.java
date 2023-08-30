package com.dougss.todo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Boolean done = false;
    @ManyToOne
    private User user;
}
