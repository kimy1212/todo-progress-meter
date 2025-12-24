package com.github.kimy12.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.kimy12.todo.dto.GetTodoTabsByUserRequest;
import com.github.kimy12.todo.dto.GetTodoTabsByUserResponse;
import com.github.kimy12.todo.dto.GetTodosByTodoTabRequest;
import com.github.kimy12.todo.dto.GetTodosByTodoTabResponse;
import com.github.kimy12.todo.dto.Todo;
import com.github.kimy12.todo.dto.TodoTab;
import com.github.kimy12.todo.exception.NotFoundException;
import com.github.kimy12.todo.repository.TodoDao;

@Service
public class TodoListService {

	private final TodoDao dao;
	
	public TodoListService(TodoDao dao) {
		this.dao = dao;
	}

	public GetTodoTabsByUserResponse getTodoTabsByUser(final GetTodoTabsByUserRequest request) {
		List<TodoTab> todoTabs = dao.getTodoTabsByUser(request.getUserId());

		GetTodoTabsByUserResponse response = new GetTodoTabsByUserResponse();
		response.setTodoTabs(todoTabs);

		return response;
	}

	public GetTodosByTodoTabResponse getTodosByTodoTab(final GetTodosByTodoTabRequest request) {
		if (!dao.existsTodoTab(request.getUserId(), request.getTodoTabId())) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}

		List<Todo> todos = dao.getTodosByUserAndTodoTab(request.getUserId(), request.getTodoTabId());

		GetTodosByTodoTabResponse response = new GetTodosByTodoTabResponse();
		response.setTodos(todos);

		return response;
	}

}
