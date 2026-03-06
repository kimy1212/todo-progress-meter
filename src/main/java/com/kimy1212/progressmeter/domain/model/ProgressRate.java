package com.kimy1212.progressmeter.domain.model;

public final class ProgressRate {

	private final int completed;

	private final int total;

	private ProgressRate(int completed, int total) {
		if (total < 0) {
			throw new IllegalArgumentException("total must be non-negative");
		}

		if (completed < 0) {
			throw new IllegalArgumentException("completed must be non-negative");
		}

		if (completed > total) {
			throw new IllegalArgumentException("completed must not exceed total");
		}

		this.completed = completed;
		this.total = total;
	}

	public static ProgressRate of(int completed, int total) {
		return new ProgressRate(completed, total);
	}

	public int percentage() {
		return (int) ((double) completed / total * 100);
	}

}
