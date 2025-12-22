package com.github.kimy12.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Todo {

	private Integer todoId;
	
	private String todoName;
	
}
