package com.kimy1212.progressmeter.domain.repository;

import java.util.List;
import java.util.Optional;

import com.kimy1212.progressmeter.domain.model.ProgressRate;
import com.kimy1212.progressmeter.domain.model.Todo;
import com.kimy1212.progressmeter.domain.model.TodoId;
import com.kimy1212.progressmeter.domain.model.TodoName;
import com.kimy1212.progressmeter.domain.model.TodoTab;
import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.model.TodoTabName;
import com.kimy1212.progressmeter.domain.model.UserId;

public interface TodoDao {

	public List<TodoTab> findTodoTabsByUserId(final UserId userId);

	public List<Todo> findTodosByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);

	public TodoTabId createTodoTab(final UserId userId, final TodoTabName todoTabName);

	public void createTodo(final UserId userId, final TodoTabId todoTabId, final TodoName todoName);

	public void updateTodoTab(final UserId userId, final TodoTabId todoTabId, final TodoTabName todoTabName);

	public void updateTodo(
			final UserId userId,
			final TodoTabId todoTabId,
			final TodoId todoId,
			final Optional<TodoName> todoName,
			final Optional<ProgressRate> progressRate);

	public int deleteTodoTabByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);

	public int deleteTodoByUserIdAndTodoTabIdAndTodoId(
			final UserId userId,
			final TodoTabId todoTabId,
			final TodoId todoId);

	public boolean existsTodoTabByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);

}
