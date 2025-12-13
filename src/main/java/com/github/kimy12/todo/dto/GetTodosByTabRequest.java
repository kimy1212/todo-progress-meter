package com.github.kimy12.todo.dto;

import lombok.Data;

@Data
public class GetTodosByTabRequest {
	
	private String userId;
	
	private Integer tabId;

}
