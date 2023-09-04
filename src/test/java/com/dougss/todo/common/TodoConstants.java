package com.dougss.todo.common;


import com.dougss.todo.dto.TodoDTO;
import com.dougss.todo.model.Todo;
import com.dougss.todo.model.User;

public class TodoConstants {
  public static final User USER = new User("douglas@dougss.com", "Afasdf123@");
  public static final User USER2 = new User("douglas2@dougss.com", "Afasdf123@");
  public static final User INVALID_USER = new User("", "");
  public static final Todo TODO = new Todo("buy Coca", USER);
  public static final Todo INVALID_TODO = new Todo("", INVALID_USER);
  public static final TodoDTO TODODTO = new TodoDTO(new Todo("buy Coca", USER));
  public static final TodoDTO INVALID_TODODTO = new TodoDTO(new Todo("", INVALID_USER));
}
