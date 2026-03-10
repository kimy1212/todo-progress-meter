package com.kimy1212.progressmeter.application.service.todo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimy1212.progressmeter.application.command.CreateTodoCommand;
import com.kimy1212.progressmeter.application.command.CreateTodoTabCommand;
import com.kimy1212.progressmeter.application.command.DeleteTodoCommand;
import com.kimy1212.progressmeter.application.command.DeleteTodoTabCommand;
import com.kimy1212.progressmeter.application.command.GetTodoTabsCommand;
import com.kimy1212.progressmeter.application.command.GetTodosCommand;
import com.kimy1212.progressmeter.application.command.UpdateTodoCommand;
import com.kimy1212.progressmeter.application.command.UpdateTodoTabCommand;
import com.kimy1212.progressmeter.application.exception.NotFoundException;
import com.kimy1212.progressmeter.application.response.TodoDto;
import com.kimy1212.progressmeter.application.response.TodoTabDto;
import com.kimy1212.progressmeter.domain.model.Todo;
import com.kimy1212.progressmeter.domain.model.TodoTab;
import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.repository.TodoDao;

@Service
public class TodoServiceImpl implements TodoService {

	private final TodoDao dao;

	public TodoServiceImpl(TodoDao dao) {
		this.dao = dao;
	}

	@Override
	public List<TodoTabDto> getTodoTabs(final GetTodoTabsCommand command) {
		List<TodoTab> todoTabs = dao.findTodoTabsByUserId(command.getUserId());

		return todoTabs.stream()
				.map(tab -> new TodoTabDto(
						tab.getTodoTabId().value(),
						tab.getTodoTabName().value()))
				.toList();
	}

	@Override
	public List<TodoDto> getTodos(final GetTodosCommand command) {
		if (!dao.existsTodoTabByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId())) {
			throw new NotFoundException("TODOタブが見つかりませんでした");
		}

		List<Todo> todos = dao.findTodosByUserIdAndTodoTabId(command.getUserId(), command.getTodoTabId());

		return todos.stream()
				.map(todo -> new TodoDto(
						todo.getTodoId().value(),
						todo.getTodoName().value(),
						todo.progressRate().percentage(),
						todo.getCompleted(),
						todo.getTotal()))
				.toList();
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
	public void updateTodoTab(final UpdateTodoTabCommand command) {
		dao.updateTodoTab(command.getUserId(), command.getTodoTabId(), command.getTodoTabName());
	}

	@Override
	@Transactional
	public void updateTodo(final UpdateTodoCommand command) {
		dao.updateTodo(
				command.getUserId(),
				command.getTodoTabId(),
				command.getTodoId(),
				command.getTodoName(),
				command.getProgressRate());
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
