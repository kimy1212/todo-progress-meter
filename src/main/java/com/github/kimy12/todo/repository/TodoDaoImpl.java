package com.github.kimy12.todo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.kimy12.todo.dto.TodoTab;
import com.github.kimy12.todo.dto.Todo;

@Repository
public class TodoDaoImpl implements TodoDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<TodoTab> getTabsByUser(final String userId) {
		String sql = """
				SELECT tab_id, tab_name
				FROM todo_tabs
				WHERE user_id = :userId
				ORDER BY tab_id
				""";

		return namedParameterJdbcTemplate.query(
				sql,
				Map.of("userId", userId),
				(rs, rowNum) -> new TodoTab(
						rs.getInt("tab_id"),
						rs.getString("tab_name")));
	}

	@Override
	public List<Todo> getTodosByUserAndTab(final String userId, final Integer tabId) {
		String sql = """
				SELECT todo_id, todo_name
				FROM todos
				WHERE user_id = :userId AND tab_id = :tabId
				ORDER BY todo_id
				""";

		return namedParameterJdbcTemplate.query(sql, Map.of("userId", userId, "tabId", tabId),
				(rs, rowNum) -> new Todo(
						rs.getInt("todo_id"),
						rs.getString("todo_name")));
	}

	@Override
	public boolean existsTodoTab(final String userId, final Integer tabId) {
		String sql = """
				SELECT EXISTS (
					SELECT 1
					FROM todo_tabs
					WHERE user_id = :userId AND tab_id = :tabId
				)
				""";

		Map<String, Object> params = Map.of(
				"userId", userId,
				"tabId", tabId);

		Boolean existsTodoTab = namedParameterJdbcTemplate.queryForObject(sql, params, Boolean.class);

		return Boolean.TRUE.equals(existsTodoTab);
	}

}
