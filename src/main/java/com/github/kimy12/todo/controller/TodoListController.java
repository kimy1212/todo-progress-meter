package com.github.kimy12.todo.controller;

import java.util.Set;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.kimy12.todo.dto.GetTodoTabsByUserRequest;
import com.github.kimy12.todo.dto.GetTodoTabsByUserResponse;
import com.github.kimy12.todo.service.TodoListService;

@Controller
public class TodoListController {
	
	private final TodoListService service;
	
	private final Validator validator;
	
	public TodoListController(TodoListService service, Validator validator) {
		this.service = service;
		this.validator = validator;
	}

	@GetMapping("/todo-list")
	public String getTodoTabsByUser(final HttpSession session, final Model model) {
		String userId = (String) session.getAttribute("userId");
		
		GetTodoTabsByUserRequest requestData = new GetTodoTabsByUserRequest();
		requestData.setUserId(userId);
		
        Set<ConstraintViolation<GetTodoTabsByUserRequest>> violations = validator.validate(requestData);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
		
		GetTodoTabsByUserResponse responseData = service.getTodoTabsByUser(requestData);
		model.addAttribute("todoTabs", responseData.getTodoTabs());
		
		return "todo-list";
	}
	
}
