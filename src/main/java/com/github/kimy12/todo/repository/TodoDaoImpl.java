package com.github.kimy12.todo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.kimy12.todo.dto.Todo;
import com.github.kimy12.todo.dto.TodoTab;

@Repository
public class TodoDaoImpl implements TodoDao {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public TodoDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	@Override
	public List<TodoTab> findTodoTabsByUserId(final String userId) {
		String sql = """
				SELECT todo_tab_id, todo_tab_name
				FROM todo_tabs
				WHERE user_id = :userId
				ORDER BY todo_tab_id
				""";

		return namedParameterJdbcTemplate.query(
				sql,
				Map.of("userId", userId),
				(rs, rowNum) -> new TodoTab(
						rs.getInt("todo_tab_id"),
						rs.getString("todo_tab_name")));
	}

	@Override
	public List<Todo> findTodosByUserIdAndTodoTabId(final String userId, final Integer todoTabId) {
		String sql = """
				SELECT todo_id, todo_name
				FROM todos
				WHERE user_id = :userId AND todo_tab_id = :todoTabId
				ORDER BY todo_id
				""";

		return namedParameterJdbcTemplate.query(sql, Map.of("userId", userId, "todoTabId", todoTabId),
				(rs, rowNum) -> new Todo(
						rs.getInt("todo_id"),
						rs.getString("todo_name")));
	}

	@Override
	public boolean existsTodoTabByUserIdAndTodoTabId(final String userId, final Integer todoTabId) {
		String sql = """
				SELECT EXISTS (
					SELECT 1
					FROM todo_tabs
					WHERE user_id = :userId AND todo_tab_id = :todoTabId
				)
				""";

		Map<String, Object> params = Map.of(
				"userId", userId,
				"todoTabId", todoTabId);

		Boolean existsTodoTab = namedParameterJdbcTemplate.queryForObject(sql, params, Boolean.class);

		return Boolean.TRUE.equals(existsTodoTab);
	}

}
