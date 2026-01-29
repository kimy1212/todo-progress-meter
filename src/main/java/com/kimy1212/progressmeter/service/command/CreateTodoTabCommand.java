package com.kimy1212.progressmeter.service.command;

import com.kimy1212.progressmeter.domain.valueobject.TodoTabName;
import com.kimy1212.progressmeter.domain.valueobject.UserId;

public class CreateTodoTabCommand {

	private final UserId userId;

	private final TodoTabName todoTabName;

	public CreateTodoTabCommand(UserId userId, TodoTabName todoTabName) {
		this.userId = userId;
		this.todoTabName = todoTabName;
	}

	public UserId getUserId() {
		return userId;
	}

	public TodoTabName getTodoTabName() {
		return todoTabName;
	}

}
