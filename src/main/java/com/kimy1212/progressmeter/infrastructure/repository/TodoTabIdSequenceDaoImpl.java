package com.kimy1212.progressmeter.infrastructure.repository;

import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kimy1212.progressmeter.domain.repository.TodoTabIdSequenceDao;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.UserId;

@Repository
public class TodoTabIdSequenceDaoImpl implements TodoTabIdSequenceDao {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public TodoTabIdSequenceDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public TodoTabId allocate(final UserId userId) {
		int updated = namedParameterJdbcTemplate.update(
				"""
				UPDATE todo_tab_id_sequences
				SET next_todo_tab_id = next_todo_tab_id + 1
				WHERE user_id = :userId
				""",
				Map.of("userId", userId.value()));

		// 初回
		if (updated == 0) {
			namedParameterJdbcTemplate.update(
					"""
					INSERT INTO todo_tab_id_sequences (user_id, next_todo_tab_id)
					VALUES (:userId, 2)
					""",
					Map.of("userId", userId.value()));
			return TodoTabId.of(1);
		}

		Integer id = namedParameterJdbcTemplate.queryForObject(
				"""
				SELECT next_todo_tab_id - 1
				FROM todo_tab_id_sequences
				WHERE user_id = :userId
				""",
				Map.of("userId", userId.value()),
				Integer.class);

		return TodoTabId.of(id);
	}

}
