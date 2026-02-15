package com.kimy1212.progressmeter.infrastructure.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.kimy1212.progressmeter.domain.repository.TodoDao;
import com.kimy1212.progressmeter.domain.valueobject.TodoId;
import com.kimy1212.progressmeter.domain.valueobject.TodoName;
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
				SELECT todos.todo_id, todos.todo_name
				FROM todos
				INNER JOIN todo_tabs
				ON todos.todo_tab_id = todo_tabs.todo_tab_id
				WHERE todos.todo_tab_id = :todoTabId
				AND todo_tabs.user_id = :userId
				ORDER BY todos.todo_id
				""";

		return namedParameterJdbcTemplate.query(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value()),
				(rs, rowNum) -> new TodoRow(
						rs.getInt("todo_id"),
						rs.getString("todo_name")));
	}

	@Override
	public TodoTabId createTodoTab(final UserId userId, final TodoTabName todoTabName) {
		String sql = """
				INSERT INTO todo_tabs (todo_tab_name, user_id)
				VALUES (:todoTabName, :userId)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		namedParameterJdbcTemplate.update(
				sql,
				new MapSqlParameterSource()
						.addValue("userId", userId.value())
						.addValue("todoTabName", todoTabName.value()),
				keyHolder);

		Long todoTabId = keyHolder.getKey().longValue();

		return TodoTabId.of(todoTabId);
	}

	@Override
	public void createTodo(final UserId userId, final TodoTabId todoTabId, final TodoName todoName) {
		String sql = """
				INSERT INTO todos (todo_name, todo_tab_id)
				SELECT
					:todoName,
					todo_tabs.todo_tab_id
				FROM todo_tabs
				WHERE todo_tabs.todo_tab_id = :todoTabId
				AND todo_tabs.user_id = :userId
				""";

		namedParameterJdbcTemplate.update(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value(), "todoName", todoName.value()));
	}

	@Override
	public void updateTodoTab(final UserId userId, final TodoTabId todoTabId, final TodoTabName todoTabName) {
		String sql = """
				UPDATE todo_tabs
				SET todo_tab_name = :todoTabName
				WHERE todo_tab_id = :todoTabId
				AND user_id = :userId
				""";

		namedParameterJdbcTemplate.update(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value(), "todoTabName", todoTabName.value()));
	}

	@Override
	public int deleteTodoTabByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId) {
		String sql = """
				DELETE
				FROM todo_tabs
				WHERE todo_tab_id = :todoTabId
				AND user_id = :userId
				""";

		return namedParameterJdbcTemplate.update(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value()));
	}

	@Override
	public int deleteTodoByUserIdAndTodoTabIdAndTodoId(
			final UserId userId,
			final TodoTabId todoTabId,
			final TodoId todoId) {
		String sql = """
				DELETE
				FROM todos
				WHERE todo_id = :todoId
				AND todo_tab_id = :todoTabId
				AND user_id = :userId
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
					WHERE todo_tab_id = :todoTabId
					AND user_id = :userId
				)
				""";

		Boolean existsTodoTab = namedParameterJdbcTemplate.queryForObject(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value()),
				Boolean.class);

		return Boolean.TRUE.equals(existsTodoTab);
	}

}
