package com.dougss.todo.common;


import com.dougss.todo.dto.TodoDTO;
import com.dougss.todo.model.Todo;
import com.dougss.todo.model.User;

public class TodoConstants {
  public static final Todo TODO = new Todo("buy Coca", new User("douglas", "senha123"));
  public static final Todo INVALID_TODO = new Todo("", new User("", ""));
  public static final TodoDTO TODODTO = new TodoDTO(new Todo("buy Coca", new User("douglas", "senha123")));
  public static final TodoDTO INVALID_TODODTO = new TodoDTO(new Todo("", new User("", "")));
  public static final User USER = new User("douglas", "senha123");
}
