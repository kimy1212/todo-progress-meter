package com.kimy1212.progressmeter.service.todo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kimy1212.progressmeter.domain.repository.TodoDao;
import com.kimy1212.progressmeter.domain.valueobject.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;
import com.kimy1212.progressmeter.service.command.GetTodosCommand;
import com.kimy1212.progressmeter.service.exception.NotFoundException;

@Service
public class TodoService {

	private final TodoDao dao;

	public TodoService(TodoDao dao) {
		this.dao = dao;
	}

	public List<TodoTabRow> getTodoTabs(final UserId userId) {
		return dao.findTodoTabsByUserId(userId);
	}

	public List<TodoRow> getTodos(final GetTodosCommand command) {
		if (!dao.existsTodoTabByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId())) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}

		return dao.findTodosByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId());
	}

}
