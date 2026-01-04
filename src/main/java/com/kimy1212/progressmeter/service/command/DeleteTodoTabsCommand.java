package com.kimy1212.progressmeter.service.command;

import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;

public class DeleteTodoTabsCommand {

	private final UserId userId;

	private final TodoTabId todoTabId;

	public DeleteTodoTabsCommand(UserId userId, TodoTabId todoTabId) {
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
