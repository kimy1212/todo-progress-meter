package com.github.kimy12.todo.repository;

import java.util.List;

import com.github.kimy12.todo.dto.TodoTab;
import com.github.kimy12.todo.dto.Todo;

public interface TodoDao {
	
	public List<TodoTab> getTabsByUser(final String userId) ;
	
	public List<Todo> getTodosByUserAndTab(final String userId, final Integer tabId);
	
	public boolean existsTodoTab(final String userId, final Integer tabId);

}
