package com.kimy1212.progressmeter.domain.repository;

import java.util.List;

import com.kimy1212.progressmeter.application.command.UpdateTodoCommand;
import com.kimy1212.progressmeter.domain.model.TodoId;
import com.kimy1212.progressmeter.domain.model.TodoName;
import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.model.TodoTabName;
import com.kimy1212.progressmeter.domain.model.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;

public interface TodoDao {

	public List<TodoTabRow> findTodoTabsByUserId(final UserId userId);

	public List<TodoRow> findTodosByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);

	public TodoTabId createTodoTab(final UserId userId, final TodoTabName todoTabName);

	public void createTodo(final UserId userId, final TodoTabId todoTabId, final TodoName todoName);

	public void updateTodoTab(final UserId userId, final TodoTabId todoTabId, final TodoTabName todoTabName);
	
	public void updateTodo(final UpdateTodoCommand command);

	public int deleteTodoTabByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);

	public int deleteTodoByUserIdAndTodoTabIdAndTodoId(
			final UserId userId,
			final TodoTabId todoTabId,
			final TodoId todoId);

	public boolean existsTodoTabByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);

}
