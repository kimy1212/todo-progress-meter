package com.kimy1212.progressmeter.domain.model;

public class TodoTabName {
	
	private static final int MAX_LENGTH = 30;

	private final String value;

	private TodoTabName(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("TodoTabName must not be blank");
		}
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("TodoTabName must be within 30 characters");
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
