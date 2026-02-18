package com.kimy1212.progressmeter.controller.dto;

import jakarta.validation.constraints.Size;

public record UpdateTodoRequest(

		@Size(max = 50)
		String todoName

) {
}
