package com.kimy1212.progressmeter.domain.valueobject;

public final class TodoTabId {

	private final int value;

	private TodoTabId(int value) {
		if (value <= 0) {
			throw new IllegalArgumentException("TodoTabId must be positive");
		}
		this.value = value;
	}

	public static TodoTabId of(int value) {
		return new TodoTabId(value);
	}

	public int value() {
		return value;
	}

}
