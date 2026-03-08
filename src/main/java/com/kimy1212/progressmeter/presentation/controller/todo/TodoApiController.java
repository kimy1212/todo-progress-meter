package com.kimy1212.progressmeter.presentation.controller.todo;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kimy1212.progressmeter.application.command.CreateTodoCommand;
import com.kimy1212.progressmeter.application.command.CreateTodoTabCommand;
import com.kimy1212.progressmeter.application.command.DeleteTodoCommand;
import com.kimy1212.progressmeter.application.command.DeleteTodoTabCommand;
import com.kimy1212.progressmeter.application.command.GetTodosCommand;
import com.kimy1212.progressmeter.application.command.UpdateTodoCommand;
import com.kimy1212.progressmeter.application.command.UpdateTodoTabCommand;
import com.kimy1212.progressmeter.application.response.TodoDto;
import com.kimy1212.progressmeter.application.service.todo.TodoService;
import com.kimy1212.progressmeter.domain.model.TodoId;
import com.kimy1212.progressmeter.domain.model.TodoName;
import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.model.TodoTabName;
import com.kimy1212.progressmeter.domain.model.UserId;
import com.kimy1212.progressmeter.presentation.dto.request.CreateTodoRequest;
import com.kimy1212.progressmeter.presentation.dto.request.CreateTodoTabRequest;
import com.kimy1212.progressmeter.presentation.dto.request.UpdateTodoRequest;
import com.kimy1212.progressmeter.presentation.dto.request.UpdateTodoTabRequest;
import com.kimy1212.progressmeter.presentation.dto.response.CreateTodoTabResponse;
import com.kimy1212.progressmeter.presentation.dto.response.TodoResponse;

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

		List<TodoDto> todos = service.getTodos(command);

		List<TodoResponse> response = todos.stream()
				.map(todo -> new TodoResponse(
						todo.todoId(),
						todo.todoName(),
						todo.progressRate(),
						todo.completed(),
						todo.total()))
				.toList();

		return response;
	}

	@PostMapping("/api/tabs")
	public CreateTodoTabResponse createTodoTab(
			@Valid @RequestBody final CreateTodoTabRequest request,
			final HttpSession session) {
		CreateTodoTabCommand command = new CreateTodoTabCommand(
				UserId.of((String) session.getAttribute("userId")),
				TodoTabName.of(request.todoTabName()));

		TodoTabId todoTabId = service.createTodoTab(command);

		return new CreateTodoTabResponse(todoTabId.value());
	}

	@PostMapping("/api/tabs/{tabId}/todos")
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

	@PatchMapping("/api/tabs/{tabId}")
	public void updateTodoTab(
			@PathVariable(name = "tabId") @NotNull @Positive final long todoTabId,
			@Valid @RequestBody final UpdateTodoTabRequest request,
			final HttpSession session) {
		UpdateTodoTabCommand command = new UpdateTodoTabCommand(
				UserId.of((String) session.getAttribute("userId")),
				TodoTabId.of(todoTabId),
				TodoTabName.of(request.todoTabName()));

		service.updateTodoTab(command);
	}

	@PatchMapping("/api/tabs/{tabId}/todos/{todoId}")
	public void updateTodo(
			@PathVariable(name = "tabId") @NotNull @Positive final long todoTabId,
			@PathVariable(name = "todoId") @NotNull @Positive final long todoId,
			@Valid @RequestBody final UpdateTodoRequest request,
			final HttpSession session) {
		UpdateTodoCommand command = new UpdateTodoCommand(
				UserId.of((String) session.getAttribute("userId")),
				TodoTabId.of(todoTabId),
				TodoId.of(todoId),
				Optional.ofNullable(request.todoName())
						.map(TodoName::of));

		service.updateTodo(command);
	}

	@DeleteMapping("/api/tabs/{tabId}")
	public void deleteTodoTab(
			@PathVariable(name = "tabId") @NotNull @Positive final long todoTabId,
			final HttpSession session) {
		DeleteTodoTabCommand command = new DeleteTodoTabCommand(
				UserId.of((String) session.getAttribute("userId")),
				TodoTabId.of(todoTabId));

		service.deleteTodoTab(command);
	}

	@DeleteMapping("/api/tabs/{tabId}/todos/{todoId}")
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
