package com.kimy1212.progressmeter.controller.todo;

import java.util.Set;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kimy1212.progressmeter.controller.dto.GetTodosByTodoTabRequest;
import com.kimy1212.progressmeter.controller.dto.GetTodosByTodoTabResponse;
import com.kimy1212.progressmeter.service.todo.TodoService;

@RestController
@Validated
public class TodoApiController {

	private final TodoService service;

	private final Validator validator;

	public TodoApiController(TodoService service, Validator validator) {
		this.service = service;
		this.validator = validator;
	}

	@GetMapping("/api/tabs/{tabId}/todos")
	public GetTodosByTodoTabResponse getTodos(
			@PathVariable(name = "tabId") @NotNull @Positive final Integer todoTabId,
			final HttpSession session,
			final Model model) {
		String userId = (String) session.getAttribute("userId");

		GetTodosByTodoTabRequest request = new GetTodosByTodoTabRequest();
		request.setUserId(userId);
		request.setTodoTabId(todoTabId);

		Set<ConstraintViolation<GetTodosByTodoTabRequest>> violations = validator.validate(request);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		GetTodosByTodoTabResponse response = service.getTodos(request);

		return response;
	}

}
