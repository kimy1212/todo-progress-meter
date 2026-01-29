package com.kimy1212.progressmeter.domain.valueobject;

public final class TodoName {
	
	private static final int MAX_LENGTH = 50;

	private final String value;

	private TodoName(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("TodoName must not be blank");
		}
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("TodoName must be within 50 characters");
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
