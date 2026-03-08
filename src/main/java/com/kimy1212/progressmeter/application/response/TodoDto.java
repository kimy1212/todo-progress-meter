package com.kimy1212.progressmeter.application.response;

public record TodoDto(

		long todoId,

		String todoName,

		int progressRate,
		
		int completed,
		
		int total

) {
}
