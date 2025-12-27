package com.kimy1212.progressmeter.controller.todo;

import java.util.Set;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kimy1212.progressmeter.controller.dto.GetTodoTabsByUserRequest;
import com.kimy1212.progressmeter.controller.dto.GetTodoTabsByUserResponse;
import com.kimy1212.progressmeter.service.todo.TodoService;

@Controller
public class TodoController {

	private final TodoService service;

	private final Validator validator;

	public TodoController(TodoService service, Validator validator) {
		this.service = service;
		this.validator = validator;
	}

	@GetMapping("/")
	public String getTodoTabs(final HttpSession session, final Model model) {
		session.setAttribute("userId", "550e8400-e29b-41d4-a716-446655440000");
		String userId = (String) session.getAttribute("userId");

		GetTodoTabsByUserRequest request = new GetTodoTabsByUserRequest();
		request.setUserId(userId);

		Set<ConstraintViolation<GetTodoTabsByUserRequest>> violations = validator.validate(request);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		GetTodoTabsByUserResponse response = service.getTodoTabs(request);
		model.addAttribute("todoTabs", response.getTodoTabs());

		return "todo-list";
	}

}
