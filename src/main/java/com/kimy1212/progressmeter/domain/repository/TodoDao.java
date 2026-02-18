package com.kimy1212.progressmeter.domain.repository;

import java.util.List;

import com.kimy1212.progressmeter.domain.valueobject.TodoId;
import com.kimy1212.progressmeter.domain.valueobject.TodoName;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabName;
import com.kimy1212.progressmeter.domain.valueobject.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;
import com.kimy1212.progressmeter.service.command.UpdateTodoCommand;

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
