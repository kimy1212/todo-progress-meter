package com.kimy1212.progressmeter.domain.repository;

import java.util.List;

import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;

public interface TodoDao {
	
	public List<TodoTabRow> findTodoTabsByUserId(final UserId userId) ;
	
	public List<TodoRow> findTodosByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);
	
	public boolean existsTodoTabByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId);

}
