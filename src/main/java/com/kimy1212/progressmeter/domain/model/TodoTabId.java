package com.kimy1212.progressmeter.domain.model;

public final class TodoTabId {

	private final long value;

	private TodoTabId(long value) {
		if (value <= 0) {
			throw new IllegalArgumentException("TodoTabId must be positive");
		}
		this.value = value;
	}

	public static TodoTabId of(long value) {
		return new TodoTabId(value);
	}

	public long value() {
		return value;
	}

}
