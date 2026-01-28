package com.kimy1212.progressmeter.domain.repository;

import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;

public interface TodoTabIdSequenceDao {

	public TodoTabId allocate(final UserId userId);
	
}
