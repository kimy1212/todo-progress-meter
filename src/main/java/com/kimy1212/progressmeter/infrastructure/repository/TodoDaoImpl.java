package com.kimy1212.progressmeter.infrastructure.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.kimy1212.progressmeter.domain.model.ProgressRate;
import com.kimy1212.progressmeter.domain.model.Todo;
import com.kimy1212.progressmeter.domain.model.TodoId;
import com.kimy1212.progressmeter.domain.model.TodoName;
import com.kimy1212.progressmeter.domain.model.TodoTab;
import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.model.TodoTabName;
import com.kimy1212.progressmeter.domain.model.UserId;
import com.kimy1212.progressmeter.domain.repository.TodoDao;

@Repository
public class TodoDaoImpl implements TodoDao {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public TodoDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<TodoTab> findTodoTabsByUserId(final UserId userId) {
		String sql = """
				SELECT todo_tab_id, todo_tab_name
				FROM todo_tabs
				WHERE user_id = :userId
				ORDER BY todo_tab_id
				""";

		return namedParameterJdbcTemplate.query(
				sql,
				Map.of("userId", userId.value()),
				(rs, rowNum) -> new TodoTab(
						TodoTabId.of(rs.getLong("todo_tab_id")),
						TodoTabName.of(rs.getString("todo_tab_name"))));
	}

	@Override
	public Optional<List<Todo>> findTodosByUserIdAndTodoTabId(final UserId userId, final TodoTabId todoTabId) {
		String sql = """
				SELECT
					todos.todo_id,
					todos.todo_name,
					todos.progress_total,
					todos.progress_completed
				FROM todo_tabs
				LEFT JOIN todos
				ON todos.todo_tab_id = todo_tabs.todo_tab_id
				WHERE todo_tabs.todo_tab_id = :todoTabId
				AND todo_tabs.user_id = :userId
				ORDER BY todos.todo_id
				""";

		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value()));

		if (rows.isEmpty()) {
			return Optional.empty();
		}

		List<Todo> todos = rows.stream()
				.filter(row -> row.get("todo_id") != null)
				.map(row -> new Todo(
						TodoId.of(((Number) row.get("todo_id")).longValue()),
						TodoName.of((String) row.get("todo_name")),
						((Number) row.get("progress_completed")).intValue(),
						((Number) row.get("progress_total")).intValue()))
				.toList();

		return Optional.of(todos);
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
				keyHolder,
				new String[] { "todo_tab_id" });

		Number key = keyHolder.getKey();
		if (key == null)
			throw new IllegalStateException("Failed to retrieve generated key after INSERT");

		return TodoTabId.of(key.longValue());
	}

	@Override
	public int createTodo(final UserId userId, final TodoTabId todoTabId, final TodoName todoName) {
		String sql = """
				INSERT INTO todos (todo_name, todo_tab_id)
				SELECT
					:todoName,
					todo_tabs.todo_tab_id
				FROM todo_tabs
				WHERE todo_tabs.todo_tab_id = :todoTabId
				AND todo_tabs.user_id = :userId
				""";

		return namedParameterJdbcTemplate.update(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value(), "todoName", todoName.value()));
	}

	@Override
	public int updateTodoTab(final UserId userId, final TodoTabId todoTabId, final TodoTabName todoTabName) {
		String sql = """
				UPDATE todo_tabs
				SET todo_tab_name = :todoTabName
				WHERE todo_tab_id = :todoTabId
				AND user_id = :userId
				""";

		return namedParameterJdbcTemplate.update(
				sql,
				Map.of("userId", userId.value(), "todoTabId", todoTabId.value(), "todoTabName", todoTabName.value()));
	}

	@Override
	public int updateTodo(
			final UserId userId,
			final TodoTabId todoTabId,
			final TodoId todoId,
			final Optional<TodoName> todoName,
			final Optional<ProgressRate> progressRate) {
		StringBuilder sql = new StringBuilder("""
				UPDATE todos
				SET
				""");
		Map<String, Object> params = new HashMap<>();
		List<String> sets = new ArrayList<>();

		if (todoName.isPresent()) {
			sets.add("todo_name = :todoName");
			params.put("todoName", todoName.get().value());
		}

		if (progressRate.isPresent()) {
			sets.add("progress_completed = :completed");
			sets.add("progress_total = :total");
			params.put("completed", progressRate.get().getCompleted());
			params.put("total", progressRate.get().getTotal());
		}

		if (sets.isEmpty()) {
			throw new IllegalArgumentException("At least one field must be specified for update");
		}

		sql.append(String.join(",", sets));
		sql.append("\n");

		sql.append("""
				WHERE todo_id = :todoId
				AND EXISTS (
					SELECT 1
					FROM todo_tabs
					WHERE todo_tabs.todo_tab_id = :todoTabId
					AND todo_tabs.user_id = :userId
					AND todo_tabs.todo_tab_id = todos.todo_tab_id
				)
				""");

		params.put("todoId", todoId.value());
		params.put("todoTabId", todoTabId.value());
		params.put("userId", userId.value());

		return namedParameterJdbcTemplate.update(sql.toString(), params);
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
				AND EXISTS (
					SELECT 1
					FROM todo_tabs
					WHERE todo_tabs.todo_tab_id = :todoTabId
					AND todo_tabs.user_id = :userId
				)
				""";

		return namedParameterJdbcTemplate.update(
				sql,
				Map.of(
						"userId", userId.value(),
						"todoTabId", todoTabId.value(),
						"todoId", todoId.value()));
	}

}
