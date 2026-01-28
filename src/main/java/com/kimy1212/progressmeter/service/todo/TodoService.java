package com.kimy1212.progressmeter.service.todo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimy1212.progressmeter.domain.repository.TodoDao;
import com.kimy1212.progressmeter.domain.repository.TodoTabIdSequenceDao;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;
import com.kimy1212.progressmeter.service.command.CreateTodoCommand;
import com.kimy1212.progressmeter.service.command.CreateTodoTabCommand;
import com.kimy1212.progressmeter.service.command.DeleteTodoCommand;
import com.kimy1212.progressmeter.service.command.DeleteTodoTabCommand;
import com.kimy1212.progressmeter.service.command.GetTodoTabsCommand;
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

	public List<TodoTabRow> getTodoTabs(final GetTodoTabsCommand command) {
		return dao.findTodoTabsByUserId(command.getUserId());
	}

	public List<TodoRow> getTodos(final GetTodosCommand command) {
		if (!dao.existsTodoTabByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId())) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}

		return dao.findTodosByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId());
	}

	@Transactional
	public TodoTabId createTodoTab(final CreateTodoTabCommand command) {
		TodoTabId todoTabId = todoTabIdSequenceDao.allocate(command.getUserId());
		dao.createTodoTab(command.getUserId(), todoTabId, command.getTodoTabName());

		return todoTabId;
	}

	@Transactional
	public void createTodo(final CreateTodoCommand command) {
		dao.createTodo(command.getUserId(), command.getTodoTabId(), command.getTodoName());
	}

	@Transactional
	public void deleteTodoTab(final DeleteTodoTabCommand command) {
		int deleted = dao.deleteTodoTabByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId());

		if (deleted == 0) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}
	}

	@Transactional
	public void deleteTodo(final DeleteTodoCommand command) {
		int deleted = dao.deleteTodoByUserIdAndTodoTabIdAndTodoId(
				command.getUserId(),
				command.getTodoTabId(),
				command.getTodoId());

		if (deleted == 0) {
			throw new NotFoundException("TODOが見つかりませんでした");
		}
	}

}
