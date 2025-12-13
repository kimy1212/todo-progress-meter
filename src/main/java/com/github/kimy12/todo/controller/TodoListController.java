package com.github.kimy12.todo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.kimy12.todo.dto.GetTabsByUserRequest;
import com.github.kimy12.todo.dto.GetTabsByUserResponse;
import com.github.kimy12.todo.service.TodoListService;

@Controller
public class TodoListController {
	
	@Autowired
	private TodoListService service;

	@GetMapping("/todo-list")
	public String getTabsByUser(final HttpSession session, final Model model) {
		String userId = (String) session.getAttribute("userId");
		
		GetTabsByUserRequest requestData = new GetTabsByUserRequest();
		requestData.setUserId(userId);
		
		GetTabsByUserResponse responseData = service.getTabsByUser(requestData);
		model.addAttribute("tabs", responseData.getTabs());
		
		return "todo-list";
	}
	
}
