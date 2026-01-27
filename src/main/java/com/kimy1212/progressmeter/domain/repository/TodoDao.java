package com.kimy1212.progressmeter.domain.repository;

import java.util.List;

import com.kimy1212.progressmeter.domain.valueobject.TodoId;
import com.kimy1212.progressmeter.domain.valueobject.TodoName;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabName;
import com.kimy1212.progressmeter.domain.valueobject.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;

public interface TodoDao {

	public List<TodoTabRow> findTodoTabsByUserId(final UserId userId);

	public List<TodoRow> findTodosByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);
	
	public void createTodoTab(final UserId userId, final TodoTabId todoTabId, final TodoTabName todoTabName);
	
	public void createTodo(final UserId userId, final TodoTabId todoTabId, final TodoName todoName);

	public int deleteTodoTabsByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);

	public int deleteTodosByUserIdAndTodoTabIdAndTodoId(
			final UserId userId,
			final TodoTabId todoTabId,
			final TodoId todoId);

	public boolean existsTodoTabByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);

}
