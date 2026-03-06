package com.kimy1212.progressmeter.domain.model;

public final class TodoId {

	private final long value;

	private TodoId(long value) {
		if (value <= 0) {
			throw new IllegalArgumentException("TodoId must be positive");
		}
		this.value = value;
	}

	public static TodoId of(long value) {
		return new TodoId(value);
	}

	public long value() {
		return value;
	}

}
