package com.kimy1212.progressmeter.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTodoTabRequest(

		@NotBlank
		@Size(max = 255)
		String todoTabName

) {
}
