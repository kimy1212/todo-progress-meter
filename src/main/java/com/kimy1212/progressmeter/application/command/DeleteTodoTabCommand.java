package com.kimy1212.progressmeter.application.command;

import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.model.UserId;

public class DeleteTodoTabCommand {

	private final UserId userId;

	private final TodoTabId todoTabId;

	public DeleteTodoTabCommand(UserId userId, TodoTabId todoTabId) {
		this.userId = userId;
		this.todoTabId = todoTabId;
	}

	public UserId getUserId() {
		return userId;
	}

	public TodoTabId getTodoTabId() {
		return todoTabId;
	}

}
