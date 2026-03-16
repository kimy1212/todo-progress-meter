package com.kimy1212.progressmeter.domain.model;

public class TodoTabName {

	private final String value;

	private TodoTabName(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("TodoTabName must not be blank");
		}

		this.value = value;
	}

	public static TodoTabName of(String value) {
		return new TodoTabName(value);
	}

	public String value() {
		return value;
	}

}
