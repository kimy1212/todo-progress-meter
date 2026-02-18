package com.kimy1212.progressmeter.service.command;

import java.util.Optional;

import com.kimy1212.progressmeter.domain.valueobject.TodoId;
import com.kimy1212.progressmeter.domain.valueobject.TodoName;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;

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
