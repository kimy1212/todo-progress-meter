package com.kimy1212.progressmeter.presentation.dto.response;

public record TodoResponse(

		long todoId,

		String todoName,
		
		int progressRate

) {
}
