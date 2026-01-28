package com.kimy1212.progressmeter.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoTabRequest(

		@NotBlank
		@Size(max = 30)
		String todoTabName

) {
}
