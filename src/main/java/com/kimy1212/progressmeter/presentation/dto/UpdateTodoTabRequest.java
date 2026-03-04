package com.kimy1212.progressmeter.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTodoTabRequest(

		@NotBlank
		@Size(max = 30)
		String todoTabName

) {
}
