package com.github.kimy12.todo.repository;

import java.util.List;

import com.github.kimy12.todo.dto.Todo;
import com.github.kimy12.todo.dto.TodoTab;

public interface TodoDao {
	
	public List<TodoTab> getTodoTabsByUser(final String userId) ;
	
	public List<Todo> getTodosByUserAndTodoTab(final String userId, final Integer todoTabId);
	
	public boolean existsTodoTab(final String userId, final Integer todoTabId);

}
