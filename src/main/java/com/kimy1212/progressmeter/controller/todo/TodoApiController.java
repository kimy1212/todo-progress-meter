package com.kimy1212.progressmeter.controller.todo;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kimy1212.progressmeter.controller.dto.TodoResponse;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
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
			@PathVariable(name = "tabId") @NotNull @Positive final Integer tabId,
			final HttpSession session) {
		UserId userId = UserId.of((String) session.getAttribute("userId"));
		TodoTabId todoTabId = TodoTabId.of(tabId);

		GetTodosCommand command = new GetTodosCommand(userId, todoTabId);

		List<TodoRow> rows = service.getTodos(command);

		List<TodoResponse> response = rows.stream()
				.map(row -> new TodoResponse(
						row.todoId(),
						row.todoName()))
				.toList();

		return response;
	}

}
