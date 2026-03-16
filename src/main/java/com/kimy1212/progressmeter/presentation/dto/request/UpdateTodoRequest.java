package com.kimy1212.progressmeter.presentation.dto.request;

import jakarta.validation.constraints.Min;

public record UpdateTodoRequest(

		String todoName,

		@Min(0)
		Integer completed,

		@Min(0)
		Integer total

) {
}
