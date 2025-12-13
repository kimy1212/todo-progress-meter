package com.github.kimy12.todo.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetTodosByTabResponse {

	private List<Todo> todos;

}
