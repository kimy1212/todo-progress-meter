package com.kimy1212.progressmeter.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTodoRequest(

		@NotBlank 
		@Size(max = 50) 
		String todoName

) {
}
