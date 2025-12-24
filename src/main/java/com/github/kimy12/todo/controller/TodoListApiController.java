package com.github.kimy12.todo.controller;

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

import com.github.kimy12.todo.dto.GetTodosByTodoTabRequest;
import com.github.kimy12.todo.dto.GetTodosByTodoTabResponse;
import com.github.kimy12.todo.service.TodoListService;

@RestController
@Validated
public class TodoListApiController {

	private final TodoListService service;
	
	private final Validator validator;
	
	public TodoListApiController(TodoListService service, Validator validator) {
		this.service = service;
		this.validator = validator;
	}

	@GetMapping("/api/todo-list/{todoTabId}")
	public GetTodosByTodoTabResponse getTodosByTodoTab(
			@PathVariable(name = "todoTabId") @NotNull @Positive final Integer todoTabId,
			final HttpSession session,
			final Model model) {
		String userId = (String) session.getAttribute("userId");

		GetTodosByTodoTabRequest requestData = new GetTodosByTodoTabRequest();
		requestData.setUserId(userId);
		requestData.setTodoTabId(todoTabId);

        Set<ConstraintViolation<GetTodosByTodoTabRequest>> violations = validator.validate(requestData);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
		
		GetTodosByTodoTabResponse responseData = service.getTodosByTodoTab(requestData);

		return responseData;
	}

}
