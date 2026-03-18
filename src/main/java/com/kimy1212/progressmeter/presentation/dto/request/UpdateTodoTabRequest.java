package com.kimy1212.progressmeter.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateTodoTabRequest(

		@NotBlank
		String todoTabName

) {
}
