package com.kimy1212.progressmeter.application.command;

import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.model.TodoTabName;
import com.kimy1212.progressmeter.domain.model.UserId;

public class UpdateTodoTabCommand {
	
	private final UserId userId;

	private final TodoTabId todoTabId;

	private final TodoTabName todoTabName;

	public UpdateTodoTabCommand(UserId userId, TodoTabId todoTabId, TodoTabName todoTabName) {
		this.todoTabId = todoTabId;
		this.todoTabName = todoTabName;
		this.userId = userId;
	}
	
	public UserId getUserId() {
		return userId;
	}

	public TodoTabId getTodoTabId() {
		return todoTabId;
	}

	public TodoTabName getTodoTabName() {
		return todoTabName;
	}

}
