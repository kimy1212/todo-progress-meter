package com.kimy1212.progressmeter.service.command;

import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;

public class GetTodosCommand {

	private final UserId userId;

	private final TodoTabId todoTabId;

	public GetTodosCommand(UserId userId, TodoTabId todoTabId) {
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
