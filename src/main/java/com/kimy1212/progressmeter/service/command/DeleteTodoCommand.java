package com.kimy1212.progressmeter.service.command;

import com.kimy1212.progressmeter.domain.valueobject.TodoId;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;

public class DeleteTodoCommand {

	private final UserId userId;

	private final TodoTabId todoTabId;

	private final TodoId todoId;

	public DeleteTodoCommand(UserId userId, TodoTabId todoTabId, TodoId todoId) {
		this.userId = userId;
		this.todoTabId = todoTabId;
		this.todoId = todoId;
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

}
