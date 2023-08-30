package com.dougss.todo.repository;

import com.dougss.todo.model.Todo;
import com.dougss.todo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    ArrayList<Todo> findAllByUser(User user);
    Todo findByUserAndId(User user, Long id);

}
