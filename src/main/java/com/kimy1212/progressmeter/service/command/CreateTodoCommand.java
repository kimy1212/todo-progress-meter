package com.kimy1212.progressmeter.service.command;

import com.kimy1212.progressmeter.domain.valueobject.TodoName;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;

public class CreateTodoCommand {

	private final UserId userId;
	
	private final TodoTabId todoTabId;

	private final TodoName todoName;

	public CreateTodoCommand(UserId userId, TodoTabId todoTabId, TodoName todoName) {
		this.userId = userId;
		this.todoTabId = todoTabId;
		this.todoName = todoName;
	}

	public UserId getUserId() {
		return userId;
	}

	public TodoTabId getTodoTabId() {
		return todoTabId;
	}
	
	public TodoName getTodoName() {
		return todoName;
	}

}
