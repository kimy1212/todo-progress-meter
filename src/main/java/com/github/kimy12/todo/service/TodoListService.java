package com.github.kimy12.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.kimy12.todo.dto.GetTabsByUserRequest;
import com.github.kimy12.todo.dto.GetTabsByUserResponse;
import com.github.kimy12.todo.dto.GetTodosByTabRequest;
import com.github.kimy12.todo.dto.GetTodosByTabResponse;
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

	public GetTabsByUserResponse getTabsByUser(final GetTabsByUserRequest requestData) {
		List<TodoTab> tabs = dao.getTabsByUser(requestData.getUserId());

		GetTabsByUserResponse responseData = new GetTabsByUserResponse();
		responseData.setTabs(tabs);

		return responseData;
	}

	public GetTodosByTabResponse getTodosByTab(final GetTodosByTabRequest requestData) {
		if (!dao.existsTodoTab(requestData.getUserId(), requestData.getTabId())) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}

		List<Todo> todos = dao.getTodosByUserAndTab(requestData.getUserId(), requestData.getTabId());

		GetTodosByTabResponse responseData = new GetTodosByTabResponse();
		responseData.setTodos(todos);

		return responseData;
	}

}
