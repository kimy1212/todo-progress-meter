package com.kimy1212.progressmeter.application.service.todo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimy1212.progressmeter.application.command.CreateTodoCommand;
import com.kimy1212.progressmeter.application.command.CreateTodoTabCommand;
import com.kimy1212.progressmeter.application.command.DeleteTodoCommand;
import com.kimy1212.progressmeter.application.command.DeleteTodoTabCommand;
import com.kimy1212.progressmeter.application.command.GetTodoTabsCommand;
import com.kimy1212.progressmeter.application.command.GetTodosCommand;
import com.kimy1212.progressmeter.application.command.UpdateTodoCommand;
import com.kimy1212.progressmeter.application.command.UpdateTodoTabCommand;
import com.kimy1212.progressmeter.application.exception.NotFoundException;
import com.kimy1212.progressmeter.application.response.TodoDto;
import com.kimy1212.progressmeter.application.response.TodoTabDto;
import com.kimy1212.progressmeter.domain.model.ProgressRate;
import com.kimy1212.progressmeter.domain.model.Todo;
import com.kimy1212.progressmeter.domain.model.TodoId;
import com.kimy1212.progressmeter.domain.model.TodoName;
import com.kimy1212.progressmeter.domain.model.TodoTab;
import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.model.TodoTabName;
import com.kimy1212.progressmeter.domain.model.UserId;
import com.kimy1212.progressmeter.domain.repository.TodoDao;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

	@Mock
	private TodoDao dao;

	@InjectMocks
	private TodoServiceImpl service;

	// ---- getTodoTabs ----

	@Test
	void 取得したtodoTabsをdtoに変換して返すこと() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		List<TodoTab> todoTabs = List.of(
				new TodoTab(TodoTabId.of(1L), TodoTabName.of("タブ１")),
				new TodoTab(TodoTabId.of(2L), TodoTabName.of("タブ２")));
		when(dao.findTodoTabsByUserId(userId)).thenReturn(todoTabs);

		List<TodoTabDto> result = service.getTodoTabs(new GetTodoTabsCommand(userId));

		assertEquals(2, result.size());
		assertEquals(1L, result.get(0).todoTabId());
		assertEquals("タブ１", result.get(0).todoTabName());
		assertEquals(2L, result.get(1).todoTabId());
		assertEquals("タブ２", result.get(1).todoTabName());
	}

	// ---- getTodos ----

	@Test
	void 取得したtodosをdtoに変換して返すこと() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		List<Todo> todos = List.of(
				new Todo(TodoId.of(1L), TodoName.of("タスク１"), 0, 100),
				new Todo(TodoId.of(2L), TodoName.of("タスク２"), 1, 4));
		when(dao.findTodosByUserIdAndTodoTabId(userId, todoTabId))
				.thenReturn(Optional.of(todos));

		List<TodoDto> result = service.getTodos(new GetTodosCommand(userId, todoTabId));

		assertEquals(2, result.size());
		assertEquals(1L, result.get(0).todoId());
		assertEquals("タスク１", result.get(0).todoName());
		assertEquals(0, result.get(0).progressRate());
		assertEquals(0, result.get(0).completed());
		assertEquals(100, result.get(0).total());
		assertEquals(2L, result.get(1).todoId());
		assertEquals("タスク２", result.get(1).todoName());
		assertEquals(25, result.get(1).progressRate());
		assertEquals(1, result.get(1).completed());
		assertEquals(4, result.get(1).total());
	}

	@Test
	void 存在しないtabIdでtodo一覧を取得すると404が返ること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(99L);
		when(dao.findTodosByUserIdAndTodoTabId(userId, todoTabId))
				.thenReturn(Optional.empty());

		assertThrows(NotFoundException.class,
				() -> service.getTodos(new GetTodosCommand(userId, todoTabId)));
	}

	// ---- createTodoTab ----

	@Test
	void 取得したtodoTabIdを返すこと() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabName todoTabName = TodoTabName.of("タブ１");
		TodoTabId todoTabId = TodoTabId.of(1L);
		when(dao.createTodoTab(userId, todoTabName)).thenReturn(todoTabId);

		TodoTabId result = service.createTodoTab(new CreateTodoTabCommand(userId, todoTabName));
		assertEquals(todoTabId, result);
	}

	// ---- createTodo ----

	@Test
	void todoが正常に作成されること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		TodoName todoName = TodoName.of("タスク１");
		when(dao.createTodo(userId, todoTabId, todoName)).thenReturn(1);

		assertDoesNotThrow(
				() -> service.createTodo(new CreateTodoCommand(userId, todoTabId, todoName)));
		verify(dao).createTodo(userId, todoTabId, todoName);
	}

	@Test
	void 存在しないtabIdでtodoを作成すると404が返ること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		TodoName todoName = TodoName.of("タスク１");
		when(dao.createTodo(userId, todoTabId, todoName)).thenReturn(0);

		assertThrows(NotFoundException.class,
				() -> service.createTodo(new CreateTodoCommand(userId, todoTabId, todoName)));
	}

	// ---- updateTodoTab ----

	@Test
	void todoTabが正常に更新されること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		TodoTabName todoTabName = TodoTabName.of("タブ１");
		when(dao.updateTodoTab(userId, todoTabId, todoTabName)).thenReturn(1);

		assertDoesNotThrow(
				() -> service.updateTodoTab(new UpdateTodoTabCommand(userId, todoTabId, todoTabName)));
		verify(dao).updateTodoTab(userId, todoTabId, todoTabName);
	}

	@Test
	void 存在しないtabIdでtodoTabを更新すると404が返ること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		TodoTabName todoTabName = TodoTabName.of("タブ１");
		when(dao.updateTodoTab(userId, todoTabId, todoTabName)).thenReturn(0);

		assertThrows(NotFoundException.class,
				() -> service.updateTodoTab(new UpdateTodoTabCommand(userId, todoTabId, todoTabName)));
	}

	// ---- updateTodo ----

	@Test
	void todoNameが更新できること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		TodoId todoId = TodoId.of(1L);
		Optional<TodoName> todoName = Optional.of(TodoName.of("タスク１"));
		when(dao.updateTodo(userId, todoTabId, todoId, todoName, Optional.empty())).thenReturn(1);

		assertDoesNotThrow(
				() -> service.updateTodo(
						new UpdateTodoCommand(userId, todoTabId, todoId, todoName, Optional.empty())));
		verify(dao).updateTodo(userId, todoTabId, todoId, todoName, Optional.empty());
	}

	@Test
	void progressRateが更新できること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		TodoId todoId = TodoId.of(1L);
		Optional<ProgressRate> progressRate = Optional.of(ProgressRate.of(50, 100));
		when(dao.updateTodo(userId, todoTabId, todoId, Optional.empty(), progressRate)).thenReturn(1);

		assertDoesNotThrow(
				() -> service.updateTodo(
						new UpdateTodoCommand(userId, todoTabId, todoId, Optional.empty(), progressRate)));
		verify(dao).updateTodo(userId, todoTabId, todoId, Optional.empty(), progressRate);
	}

	@Test
	void 存在しないtodoIdでtodoを更新すると404が返ること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		TodoId todoId = TodoId.of(1L);
		Optional<TodoName> todoName = Optional.of(TodoName.of("タスク１"));
		when(dao.updateTodo(userId, todoTabId, todoId, todoName, Optional.empty())).thenReturn(0);

		assertThrows(NotFoundException.class,
				() -> service.updateTodo(
						new UpdateTodoCommand(userId, todoTabId, todoId, todoName, Optional.empty())));
	}

	// ---- deleteTodoTab ----

	@Test
	void todoTabが正常に削除されること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		when(dao.deleteTodoTabByUserIdAndTodoTabId(userId, todoTabId)).thenReturn(1);

		assertDoesNotThrow(
				() -> service.deleteTodoTab(new DeleteTodoTabCommand(userId, todoTabId)));
		verify(dao).deleteTodoTabByUserIdAndTodoTabId(userId, todoTabId);
	}

	@Test
	void 存在しないtabIdでtodoTabを削除すると404が返ること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		when(dao.deleteTodoTabByUserIdAndTodoTabId(userId, todoTabId)).thenReturn(0);

		assertThrows(NotFoundException.class,
				() -> service.deleteTodoTab(new DeleteTodoTabCommand(userId, todoTabId)));
	}

	// ---- deleteTodo ----

	@Test
	void todoが正常に削除されること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		TodoId todoId = TodoId.of(1L);
		when(dao.deleteTodoByUserIdAndTodoTabIdAndTodoId(userId, todoTabId, todoId)).thenReturn(1);

		assertDoesNotThrow(
				() -> service.deleteTodo(new DeleteTodoCommand(userId, todoTabId, todoId)));
		verify(dao).deleteTodoByUserIdAndTodoTabIdAndTodoId(userId, todoTabId, todoId);
	}

	@Test
	void 存在しないtodoIdでtodoを削除すると404が返ること() {
		UserId userId = new UserId("550e8400-e29b-41d4-a716-446655440000");
		TodoTabId todoTabId = TodoTabId.of(1L);
		TodoId todoId = TodoId.of(1L);
		when(dao.deleteTodoByUserIdAndTodoTabIdAndTodoId(userId, todoTabId, todoId)).thenReturn(0);

		assertThrows(NotFoundException.class,
				() -> service.deleteTodo(new DeleteTodoCommand(userId, todoTabId, todoId)));
	}

}
