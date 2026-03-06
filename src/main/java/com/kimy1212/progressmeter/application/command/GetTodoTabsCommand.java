package com.kimy1212.progressmeter.application.command;

import com.kimy1212.progressmeter.domain.model.UserId;

public class GetTodoTabsCommand {

	private final UserId userId;

	public GetTodoTabsCommand(UserId userId) {
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

}
