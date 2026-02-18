package com.kimy1212.progressmeter.service.command;

import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabName;
import com.kimy1212.progressmeter.domain.valueobject.UserId;

public class UpdateTodoTabCommand {
	
	private final UserId userId;

	private final TodoTabId todoTabId;

	private final TodoTabName todoTabName;

	public UpdateTodoTabCommand(UserId userId, TodoTabId todoTabId, TodoTabName todoTabName) {
		this.todoTabId = todoTabId;
		this.todoTabName = todoTabName;
		this.userId = userId;
	}

	public TodoTabId getTodoTabId() {
		return todoTabId;
	}

	public TodoTabName getTodoTabName() {
		return todoTabName;
	}

	public UserId getUserId() {
		return userId;
	}

}
