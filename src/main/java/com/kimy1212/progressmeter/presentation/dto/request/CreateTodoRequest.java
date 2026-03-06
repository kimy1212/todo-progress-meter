package com.kimy1212.progressmeter.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTodoRequest(

		@NotBlank 
		@Size(max = 50) 
		String todoName

) {
}
