package com.kimy1212.progressmeter.application.service.todo;

import java.util.List;

import com.kimy1212.progressmeter.application.command.CreateTodoCommand;
import com.kimy1212.progressmeter.application.command.CreateTodoTabCommand;
import com.kimy1212.progressmeter.application.command.DeleteTodoCommand;
import com.kimy1212.progressmeter.application.command.DeleteTodoTabCommand;
import com.kimy1212.progressmeter.application.command.GetTodoTabsCommand;
import com.kimy1212.progressmeter.application.command.GetTodosCommand;
import com.kimy1212.progressmeter.application.command.UpdateTodoCommand;
import com.kimy1212.progressmeter.application.command.UpdateTodoTabCommand;
import com.kimy1212.progressmeter.application.response.TodoTabDto;
import com.kimy1212.progressmeter.application.response.TodoDto;
import com.kimy1212.progressmeter.domain.model.TodoTabId;

public interface TodoService {

	public List<TodoTabDto> getTodoTabs(final GetTodoTabsCommand command);

	public List<TodoDto> getTodos(final GetTodosCommand command);

	public TodoTabId createTodoTab(final CreateTodoTabCommand command);

	public void createTodo(final CreateTodoCommand command);

	public void updateTodoTab(final UpdateTodoTabCommand command);
	
	public void updateTodo(final UpdateTodoCommand command);

	public void deleteTodoTab(final DeleteTodoTabCommand command);

	public void deleteTodo(final DeleteTodoCommand command);

}
