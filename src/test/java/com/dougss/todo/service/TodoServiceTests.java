package com.dougss.todo.service;

import com.dougss.todo.dto.TodoDTO;
import com.dougss.todo.model.Todo;
import com.dougss.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.dougss.todo.common.TodoConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ActiveProfiles("testdb")
@ExtendWith(MockitoExtension.class)
class TodoServiceTests {

	@InjectMocks
	private TodoService todoService;

	@Mock
	private TodoRepository todoRepository;

	@Test
	public void saveTodoWithValidDataReturnsTodo() {

		Todo todoTemp = new Todo();
		todoTemp.setDescription("buy Coca");
		todoTemp.setUser(USER);

		TodoDTO todoDTOTemp = new TodoDTO();
		todoDTOTemp.setDescription(todoTemp.getDescription());

		when(todoRepository.save(todoTemp)).thenReturn(todoTemp);
		Todo objectTodo = todoService.save(todoDTOTemp, USER);

		assertThat(objectTodo.getDescription()).isEqualTo(todoTemp.getDescription());
	}

	@Test
	public void saveTodoWithInvalidDataThrowsException() {
		when(todoRepository.save(INVALID_TODO)).thenThrow(RuntimeException.class);
		assertThatThrownBy(() -> todoService.save(INVALID_TODODTO, USER)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getTodoByExistingIdReturnsTodo() {
		when(todoRepository.findById(1L)).thenReturn(Optional.of(TODO));
		Todo objectTodo = todoService.getTodoById(1L);
		assertThat(objectTodo).isNotNull();
		assertThat(objectTodo).isEqualTo(TODO);
	}

	@Test
	public void getTodoByUnexistingIdReturnsNull() {
		when(todoRepository.findById(1L)).thenReturn(Optional.empty());
		Todo objectTodo = todoService.getTodoById(1L);
		assertThat(objectTodo).isNull();
	}
}
