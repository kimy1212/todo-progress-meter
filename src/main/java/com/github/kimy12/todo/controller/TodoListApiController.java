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

import com.github.kimy12.todo.dto.GetTodosByTabRequest;
import com.github.kimy12.todo.dto.GetTodosByTabResponse;
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

	@GetMapping("/api/todo-list/{tabId}")
	public GetTodosByTabResponse getTodosByTab(
			@PathVariable(name = "tabId") @NotNull @Positive final Integer tabId,
			final HttpSession session,
			final Model model) {
		String userId = (String) session.getAttribute("userId");

		GetTodosByTabRequest requestData = new GetTodosByTabRequest();
		requestData.setUserId(userId);
		requestData.setTabId(tabId);

        Set<ConstraintViolation<GetTodosByTabRequest>> violations = validator.validate(requestData);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
		
		GetTodosByTabResponse responseData = service.getTodosByTab(requestData);

		return responseData;
	}

}
