package com.kimy1212.progressmeter.service.todo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimy1212.progressmeter.domain.repository.TodoDao;
import com.kimy1212.progressmeter.domain.repository.TodoTabIdSequenceDao;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;
import com.kimy1212.progressmeter.service.command.CreateTodoCommand;
import com.kimy1212.progressmeter.service.command.CreateTodoTabsCommand;
import com.kimy1212.progressmeter.service.command.DeleteTodoTabsCommand;
import com.kimy1212.progressmeter.service.command.DeleteTodosCommand;
import com.kimy1212.progressmeter.service.command.GetTodosCommand;
import com.kimy1212.progressmeter.service.exception.NotFoundException;

@Service
public class TodoService {

	private final TodoDao dao;

	private final TodoTabIdSequenceDao todoTabIdSequenceDao;

	public TodoService(TodoDao dao, TodoTabIdSequenceDao todoTabIdSequenceDao) {
		this.dao = dao;
		this.todoTabIdSequenceDao = todoTabIdSequenceDao;
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

	@Transactional
	public TodoTabId createTodoTab(final CreateTodoTabsCommand command) {
		TodoTabId todoTabId = todoTabIdSequenceDao.allocate(command.getUserId());
		dao.createTodoTab(command.getUserId(), todoTabId, command.getTodoTabName());

		return todoTabId;
	}

	@Transactional
	public void createTodo(final CreateTodoCommand command) {
		dao.createTodo(command.getUserId(), command.getTodoTabId(), command.getTodoName());
	}

	@Transactional
	public void deleteTodoTabs(final DeleteTodoTabsCommand command) {
		int deleted = dao.deleteTodoTabsByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId());

		if (deleted == 0) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}
	}

	@Transactional
	public void deleteTodos(final DeleteTodosCommand command) {
		int deleted = dao.deleteTodosByUserIdAndTodoTabIdAndTodoId(
				command.getUserId(),
				command.getTodoTabId(),
				command.getTodoId());

		if (deleted == 0) {
			throw new NotFoundException("TODOが見つかりませんでした");
		}
	}

}
