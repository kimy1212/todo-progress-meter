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

	public GetTodoTabsByUserResponse getTodoTabsByUser(final GetTodoTabsByUserRequest requestData) {
		List<TodoTab> todoTabs = dao.getTodoTabsByUser(requestData.getUserId());

		GetTodoTabsByUserResponse responseData = new GetTodoTabsByUserResponse();
		responseData.setTodoTabs(todoTabs);

		return responseData;
	}

	public GetTodosByTodoTabResponse getTodosByTodoTab(final GetTodosByTodoTabRequest requestData) {
		if (!dao.existsTodoTab(requestData.getUserId(), requestData.getTodoTabId())) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}

		List<Todo> todos = dao.getTodosByUserAndTodoTab(requestData.getUserId(), requestData.getTodoTabId());

		GetTodosByTodoTabResponse responseData = new GetTodosByTodoTabResponse();
		responseData.setTodos(todos);

		return responseData;
	}

}
