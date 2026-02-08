package com.kimy1212.progressmeter.service.todo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimy1212.progressmeter.domain.repository.TodoDao;
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
public class TodoServiceImpl implements TodoService {

	private final TodoDao dao;

	public TodoServiceImpl(TodoDao dao) {
		this.dao = dao;
	}

	@Override
	public List<TodoTabRow> getTodoTabs(final GetTodoTabsCommand command) {
		return dao.findTodoTabsByUserId(command.getUserId());
	}

	@Override
	public List<TodoRow> getTodos(final GetTodosCommand command) {
		if (!dao.existsTodoTabByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId())) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}

		return dao.findTodosByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId());
	}

	@Override
	@Transactional
	public TodoTabId createTodoTab(final CreateTodoTabCommand command) {
		return dao.createTodoTab(command.getUserId(), command.getTodoTabName());
	}

	@Override
	@Transactional
	public void createTodo(final CreateTodoCommand command) {
		dao.createTodo(command.getUserId(), command.getTodoTabId(), command.getTodoName());
	}

	@Override
	@Transactional
	public void deleteTodoTab(final DeleteTodoTabCommand command) {
		int deleted = dao.deleteTodoTabByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId());

		if (deleted == 0) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}
	}

	@Override
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
