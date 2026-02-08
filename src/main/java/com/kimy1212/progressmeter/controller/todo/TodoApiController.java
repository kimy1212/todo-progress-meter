package com.kimy1212.progressmeter.controller.todo;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kimy1212.progressmeter.controller.dto.CreateTodoRequest;
import com.kimy1212.progressmeter.controller.dto.CreateTodoTabRequest;
import com.kimy1212.progressmeter.controller.dto.CreateTodoTabResponse;
import com.kimy1212.progressmeter.controller.dto.TodoResponse;
import com.kimy1212.progressmeter.domain.valueobject.TodoId;
import com.kimy1212.progressmeter.domain.valueobject.TodoName;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabName;
import com.kimy1212.progressmeter.domain.valueobject.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.service.command.CreateTodoCommand;
import com.kimy1212.progressmeter.service.command.CreateTodoTabCommand;
import com.kimy1212.progressmeter.service.command.DeleteTodoCommand;
import com.kimy1212.progressmeter.service.command.DeleteTodoTabCommand;
import com.kimy1212.progressmeter.service.command.GetTodosCommand;
import com.kimy1212.progressmeter.service.todo.TodoService;

@RestController
@Validated
public class TodoApiController {

	private final TodoService service;

	public TodoApiController(TodoService service) {
		this.service = service;
	}

	@GetMapping("/api/tabs/{tabId}/todos")
	public List<TodoResponse> getTodos(
			@PathVariable(name = "tabId") @NotNull @Positive final long todoTabId,
			final HttpSession session) {
		GetTodosCommand command = new GetTodosCommand(
				UserId.of((String) session.getAttribute("userId")),
				TodoTabId.of(todoTabId));

		List<TodoRow> rows = service.getTodos(command);

		List<TodoResponse> response = rows.stream()
				.map(row -> new TodoResponse(
						row.todoId(),
						row.todoName()))
				.toList();

		return response;
	}

	@PostMapping("api/tabs")
	public CreateTodoTabResponse createTodoTab(
			@Valid @RequestBody final CreateTodoTabRequest request,
			final HttpSession session) {
		CreateTodoTabCommand command = new CreateTodoTabCommand(
				UserId.of((String) session.getAttribute("userId")),
				TodoTabName.of(request.todoTabName()));

		TodoTabId todoTabId = service.createTodoTab(command);

		return new CreateTodoTabResponse(todoTabId.value());
	}

	@PostMapping("api/tabs/{tabId}/todos")
	public void createTodo(
			@PathVariable(name = "tabId") @NotNull @Positive final long todoTabId,
			@Valid @RequestBody final CreateTodoRequest request,
			final HttpSession session) {
		CreateTodoCommand command = new CreateTodoCommand(
				UserId.of((String) session.getAttribute("userId")),
				TodoTabId.of(todoTabId),
				TodoName.of(request.todoName()));

		service.createTodo(command);
	}

	@DeleteMapping("api/tabs/{tabId}")
	public void deleteTodoTab(
			@PathVariable(name = "tabId") @NotNull @Positive final long todoTabId,
			final HttpSession session) {
		DeleteTodoTabCommand command = new DeleteTodoTabCommand(
				UserId.of((String) session.getAttribute("userId")),
				TodoTabId.of(todoTabId));

		service.deleteTodoTab(command);
	}

	@DeleteMapping("api/tabs/{tabId}/todos/{todoId}")
	public void deleteTodo(
			@PathVariable(name = "tabId") @NotNull @Positive final long todoTabId,
			@PathVariable(name = "todoId") @NotNull @Positive final long todoId,
			final HttpSession session) {
		DeleteTodoCommand command = new DeleteTodoCommand(
				UserId.of((String) session.getAttribute("userId")),
				TodoTabId.of(todoTabId),
				TodoId.of(todoId));

		service.deleteTodo(command);
	}

}
