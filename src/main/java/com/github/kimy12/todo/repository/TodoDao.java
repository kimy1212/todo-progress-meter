package com.github.kimy12.todo.repository;

import java.util.List;

import com.github.kimy12.todo.dto.Todo;
import com.github.kimy12.todo.dto.TodoTab;

public interface TodoDao {
	
	public List<TodoTab> findTodoTabsByUserId(final String userId) ;
	
	public List<Todo> findTodosByUserIdAndTodoTabId(final String userId, final Integer todoTabId);
	
	public boolean existsTodoTabByUserIdAndTodoTabId(final String userId, final Integer todoTabId);

}
