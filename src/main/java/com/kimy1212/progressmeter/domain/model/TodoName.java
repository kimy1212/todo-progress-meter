package com.kimy1212.progressmeter.domain.model;

public final class TodoName {

	private final String value;

	private TodoName(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("TodoName must not be blank");
		}

		this.value = value;
	}

	public static TodoName of(String value) {
		return new TodoName(value);
	}

	public String value() {
		return value;
	}

}
