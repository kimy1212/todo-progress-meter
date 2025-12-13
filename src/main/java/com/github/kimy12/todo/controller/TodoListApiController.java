package com.github.kimy12.todo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.github.kimy12.todo.dto.GetTodosByTabRequest;
import com.github.kimy12.todo.dto.GetTodosByTabResponse;
import com.github.kimy12.todo.service.TodoListService;

@RestController
public class TodoListApiController {
	
	@Autowired
	private TodoListService service;
	
	@GetMapping("/api/todo-list/{tabId}")
	public GetTodosByTabResponse getTodosByTab(@PathVariable(name = "tabId") final Integer tabId, final HttpSession session, final Model model) {
		String userId = (String) session.getAttribute("userId");
		
		GetTodosByTabRequest requestData = new GetTodosByTabRequest();
		requestData.setUserId(userId);
		requestData.setTabId(tabId);
		
		GetTodosByTabResponse responseData = service.getTodosByTab(requestData);
	    
		return responseData;
	}

}
