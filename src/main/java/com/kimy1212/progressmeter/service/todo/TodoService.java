package com.kimy1212.progressmeter.service.todo;

import java.util.List;

import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;
import com.kimy1212.progressmeter.service.command.CreateTodoCommand;
import com.kimy1212.progressmeter.service.command.CreateTodoTabCommand;
import com.kimy1212.progressmeter.service.command.DeleteTodoCommand;
import com.kimy1212.progressmeter.service.command.DeleteTodoTabCommand;
import com.kimy1212.progressmeter.service.command.GetTodoTabsCommand;
import com.kimy1212.progressmeter.service.command.GetTodosCommand;
import com.kimy1212.progressmeter.service.command.UpdateTodoCommand;
import com.kimy1212.progressmeter.service.command.UpdateTodoTabCommand;

public interface TodoService {

	public List<TodoTabRow> getTodoTabs(final GetTodoTabsCommand command);

	public List<TodoRow> getTodos(final GetTodosCommand command);

	public TodoTabId createTodoTab(final CreateTodoTabCommand command);

	public void createTodo(final CreateTodoCommand command);

	public void updateTodoTab(final UpdateTodoTabCommand command);
	
	public void updateTodo(final UpdateTodoCommand command);

	public void deleteTodoTab(final DeleteTodoTabCommand command);

	public void deleteTodo(final DeleteTodoCommand command);

}
