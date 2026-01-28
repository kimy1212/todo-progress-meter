package com.kimy1212.progressmeter.service.command;

import com.kimy1212.progressmeter.domain.valueobject.UserId;

public class GetTodoTabsCommand {

	private final UserId userId;

	public GetTodoTabsCommand(UserId userId) {
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

}
