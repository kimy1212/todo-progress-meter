package com.kimy1212.progressmeter.infrastructure.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kimy1212.progressmeter.domain.repository.TodoDao;
import com.kimy1212.progressmeter.domain.valueobject.TodoId;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabId;
import com.kimy1212.progressmeter.domain.valueobject.TodoTabName;
import com.kimy1212.progressmeter.domain.valueobject.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoRow;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;

@Repository
public class TodoDaoImpl implements TodoDao {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public TodoDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<TodoTabRow> findTodoTabsByUserId(final UserId userId) {
		String sql = """
				SELECT todo_tab_id, todo_tab_name
				FROM todo_tabs
				WHERE user_id = :userId
				ORDER BY todo_tab_id
				""";

		return namedParameterJdbcTemplate.query(
				sql,
				Map.of("userId", userId.value()),
				(rs, rowNum) -> new TodoTabRow(
						rs.getInt("todo_tab_id"),
						rs.getString("todo_tab_name")));
	}

	@Override
	public List<TodoRow> findTodosByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId) {
		String sql = """
				SELECT todo_id, todo_name
				FROM todos
				WHERE user_id = :userId AND todo_tab_id = :todoTabId
				ORDER BY todo_id
				""";

		return namedParameterJdbcTemplate.query(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value()),
				(rs, rowNum) -> new TodoRow(
						rs.getInt("todo_id"),
						rs.getString("todo_name")));
	}

	@Override
	public void createTodoTab(final UserId userId, final TodoTabName todoTabName) {
		String sql = """
				INSERT INTO todo_tabs (user_id, todo_tab_id, todo_tab_name)
				SELECT
					:userId,
					COALESCE(MAX(todo_tab_id), 0) + 1,
					:todoTabName
				FROM todo_tabs
				WHERE user_id = :userId
				""";

		namedParameterJdbcTemplate.update(
				sql,
				Map.of("userId", userId.value(), "todoTabName", todoTabName.value()));
	}

	@Override
	public int deleteTodoTabsByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId) {
		String sql = """
				DELETE
				FROM todo_tabs
				WHERE user_id = :userId AND todo_tab_id = :todoTabId
				""";

		return namedParameterJdbcTemplate.update(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value()));
	}

	@Override
	public int deleteTodosByUserIdAndTodoTabIdAndTodoId(
			final UserId userId,
			final TodoTabId todoTabId,
			final TodoId todoId) {
		String sql = """
				DELETE
				FROM todos
				WHERE user_id = :userId AND todo_tab_id = :todoTabId AND todo_id = :todoId
				""";

		return namedParameterJdbcTemplate.update(
				sql,
				Map.of(
						"userId", userId.value(),
						"todoTabId", todoTabId.value(),
						"todoId", todoId.value()));
	}

	@Override
	public boolean existsTodoTabByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId) {
		String sql = """
				SELECT EXISTS (
					SELECT 1
					FROM todo_tabs
					WHERE user_id = :userId AND todo_tab_id = :todoTabId
				)
				""";

		Boolean existsTodoTab = namedParameterJdbcTemplate.queryForObject(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value()),
				Boolean.class);

		return Boolean.TRUE.equals(existsTodoTab);
	}

}
