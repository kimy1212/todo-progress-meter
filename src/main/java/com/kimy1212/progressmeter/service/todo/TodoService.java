package com.kimy1212.progressmeter.service.todo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kimy1212.progressmeter.controller.dto.GetTodoTabsByUserRequest;
import com.kimy1212.progressmeter.controller.dto.GetTodoTabsByUserResponse;
import com.kimy1212.progressmeter.controller.dto.GetTodosByTodoTabRequest;
import com.kimy1212.progressmeter.controller.dto.GetTodosByTodoTabResponse;
import com.kimy1212.progressmeter.controller.dto.Todo;
import com.kimy1212.progressmeter.controller.dto.TodoTab;
import com.kimy1212.progressmeter.domain.repository.TodoDao;
import com.kimy1212.progressmeter.service.exception.NotFoundException;

@Service
public class TodoService {

	private final TodoDao dao;
	
	public TodoService(TodoDao dao) {
		this.dao = dao;
	}

	public GetTodoTabsByUserResponse getTodoTabs(final GetTodoTabsByUserRequest request) {
		List<TodoTab> todoTabs = dao.findTodoTabsByUserId(request.getUserId());

		GetTodoTabsByUserResponse response = new GetTodoTabsByUserResponse();
		response.setTodoTabs(todoTabs);

		return response;
	}

	public GetTodosByTodoTabResponse getTodos(final GetTodosByTodoTabRequest request) {
		if (!dao.existsTodoTabByUserIdAndTodoTabId(request.getUserId(), request.getTodoTabId())) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}

		List<Todo> todos = dao.findTodosByUserIdAndTodoTabId(request.getUserId(), request.getTodoTabId());

		GetTodosByTodoTabResponse response = new GetTodosByTodoTabResponse();
		response.setTodos(todos);

		return response;
	}

}
