package com.kimy1212.progressmeter.presentation.controller.todo;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kimy1212.progressmeter.application.command.GetTodoTabsCommand;
import com.kimy1212.progressmeter.application.service.todo.TodoService;
import com.kimy1212.progressmeter.domain.model.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;
import com.kimy1212.progressmeter.presentation.dto.TodoTabResponse;

@Controller
public class TodoController {

	private final TodoService service;

	public TodoController(TodoService service) {
		this.service = service;
	}

	@GetMapping("/")
	public String getTodoTabs(final HttpSession session, final Model model) {
		session.setAttribute("userId", "550e8400-e29b-41d4-a716-446655440000");
		GetTodoTabsCommand command = new GetTodoTabsCommand(
				UserId.of((String) session.getAttribute("userId")));

		List<TodoTabRow> rows = service.getTodoTabs(command);

		List<TodoTabResponse> response = rows.stream()
				.map(row -> new TodoTabResponse(
						row.todoTabId(),
						row.todoTabName()))
				.toList();

		model.addAttribute("todoTabs", response);

		return "todo-list";
	}

}
