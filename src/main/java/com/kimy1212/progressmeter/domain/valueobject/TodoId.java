package com.kimy1212.progressmeter.domain.valueobject;

public final class TodoId {

	private final int value;

	private TodoId(int value) {
		if (value <= 0) {
			throw new IllegalArgumentException("TodoId must be positive");
		}
		this.value = value;
	}

	public static TodoId of(int value) {
		return new TodoId(value);
	}

	public int value() {
		return value;
	}

}
