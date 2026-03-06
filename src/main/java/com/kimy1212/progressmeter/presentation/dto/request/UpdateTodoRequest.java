package com.kimy1212.progressmeter.presentation.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateTodoRequest(

		@Size(max = 50)
		String todoName

) {
}
