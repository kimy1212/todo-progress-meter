package com.kimy1212.progressmeter.presentation.controller.todo;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kimy1212.progressmeter.application.command.GetTodoTabsCommand;
import com.kimy1212.progressmeter.application.response.TodoTabDto;
import com.kimy1212.progressmeter.application.service.todo.TodoService;
import com.kimy1212.progressmeter.infrastructure.security.UserIdResolver;
import com.kimy1212.progressmeter.presentation.dto.response.TodoTabResponse;

@Controller
public class TodoController {

	private final TodoService service;

	public TodoController(TodoService service) {
		this.service = service;
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/")
	public String getTodoTabs(@AuthenticationPrincipal OAuth2User principal, final Model model) {
		GetTodoTabsCommand command = new GetTodoTabsCommand(
				UserIdResolver.resolve(principal));

		List<TodoTabDto> todoTabs = service.getTodoTabs(command);

		List<TodoTabResponse> response = todoTabs.stream()
				.map(tab -> new TodoTabResponse(
						tab.todoTabId(),
						tab.todoTabName()))
				.toList();

		model.addAttribute("todoTabs", response);

		return "todo-list";
	}

}
