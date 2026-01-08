package com.kimy1212.progressmeter.domain.valueobject;

import java.util.UUID;

public final class UserId {

	private final String value;

	public UserId(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("UserId must not be blank");
		}

		try {
			UUID.fromString(value);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("UserId must be a valid UUID", e);
		}

		this.value = value;
	}

	public static UserId of(String value) {
		return new UserId(value);
	}

	public String value() {
		return value;
	}

}
