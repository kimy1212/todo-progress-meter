package com.kimy1212.progressmeter.presentation.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateTodoRequest(

		@Size(max = 255)
		String todoName,

		@Min(0)
		Integer completed,

		@Min(0)
		Integer total

) {

	@AssertTrue(message = "completed must not exceed total")
	public boolean isValidProgressRate() {
		if (completed == null || total == null) return true;
		return completed <= total;
	}

}
