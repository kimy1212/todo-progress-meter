package com.kimy1212.progressmeter.application.command;

import java.util.Optional;

import com.kimy1212.progressmeter.domain.model.TodoId;
import com.kimy1212.progressmeter.domain.model.TodoName;
import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.model.UserId;

public class UpdateTodoCommand {

	private final UserId userId;

	private final TodoTabId todoTabId;

	private final TodoId todoId;

	private final Optional<TodoName> todoName;

	public UpdateTodoCommand(UserId userId, TodoTabId todoTabId, TodoId todoId, Optional<TodoName> todoName) {
		this.userId = userId;
		this.todoTabId = todoTabId;
		this.todoId = todoId;
		this.todoName = todoName;
	}

	public UserId getUserId() {
		return userId;
	}

	public TodoTabId getTodoTabId() {
		return todoTabId;
	}

	public TodoId getTodoId() {
		return todoId;
	}

	public Optional<TodoName> getTodoName() {
		return todoName;
	}

}
