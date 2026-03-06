package com.kimy1212.progressmeter.domain.model;

import lombok.Getter;

@Getter
public class TodoTab {

	private final TodoTabId todoTabId;

	private final TodoTabName todoTabName;

	public TodoTab(TodoTabId todoTabId, TodoTabName todoTabName) {
		this.todoTabId = todoTabId;
		this.todoTabName = todoTabName;
	}

}
