package com.kimy1212.progressmeter.domain.model;

import lombok.Getter;

@Getter
public class Todo {

	private final TodoId todoId;

	private final TodoName todoName;

	private final int completed;

	private final int total;

	public Todo(TodoId todoId, TodoName todoName, int completed, int total) {
		this.todoId = todoId;
		this.todoName = todoName;
		this.completed = completed;
		this.total = total;
	}

	public ProgressRate progressRate() {
		return ProgressRate.of(completed, total);
	}

}
