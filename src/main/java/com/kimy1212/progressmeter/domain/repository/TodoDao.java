package com.kimy1212.progressmeter.domain.repository;

import java.util.List;

import com.kimy1212.progressmeter.controller.dto.Todo;
import com.kimy1212.progressmeter.controller.dto.TodoTab;

public interface TodoDao {
	
	public List<TodoTab> findTodoTabsByUserId(final String userId) ;
	
	public List<Todo> findTodosByUserIdAndTodoTabId(final String userId, final Integer todoTabId);
	
	public boolean existsTodoTabByUserIdAndTodoTabId(final String userId, final Integer todoTabId);

}
