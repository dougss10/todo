package com.dougss.todo.service;

import com.dougss.todo.model.Todo;
import com.dougss.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dougss.todo.common.TodoConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTests {

	@InjectMocks
	private TodoService todoService;

	@Mock
	private TodoRepository todoRepository;

	@Test
	public void saveTodo_WithValidData_ReturnsTodo() {
		when(todoRepository.save(TODO)).thenReturn(TODO);
		Todo objectTodo = todoService.save(TODODTO, USER);

		assertThat(objectTodo).isEqualTo(TODO);
	}

	@Test
	public void saveTodo_WithInvalidData_ThrowsException() {
		when(todoRepository.save(INVALID_TODO)).thenThrow(RuntimeException.class);
		assertThatThrownBy(() -> todoService.save(INVALID_TODODTO, USER)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getTodo_ByExistingId_ReturnsTodo() {
		when(todoRepository.findById(1L)).thenReturn(Optional.of(TODO));
		Todo objectTodo = todoService.getTodoById(1L);
		assertThat(objectTodo).isNotNull();
		assertThat(objectTodo).isEqualTo(TODO);
	}

	@Test
	public void getTodo_ByUnexistingId_ReturnsNull() {
		when(todoRepository.findById(1L)).thenReturn(Optional.empty());
		Todo objectTodo = todoService.getTodoById(1L);
		assertThat(objectTodo).isNull();
	}
}
