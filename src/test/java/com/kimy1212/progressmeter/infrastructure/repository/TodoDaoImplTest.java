package com.kimy1212.progressmeter.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.jdbc.Sql;

import com.kimy1212.progressmeter.domain.model.ProgressRate;
import com.kimy1212.progressmeter.domain.model.Todo;
import com.kimy1212.progressmeter.domain.model.TodoId;
import com.kimy1212.progressmeter.domain.model.TodoName;
import com.kimy1212.progressmeter.domain.model.TodoTab;
import com.kimy1212.progressmeter.domain.model.TodoTabId;
import com.kimy1212.progressmeter.domain.model.TodoTabName;
import com.kimy1212.progressmeter.domain.model.UserId;

@JdbcTest
@Import(TodoDaoImpl.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class TodoDaoImplTest {

	@Autowired
	private TodoDaoImpl dao;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private long insertTodoTab(String userId, String todoTabName) {
		var keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				"INSERT INTO todo_tabs (todo_tab_name, user_id) VALUES (:todoTabName, :userId)",
				new MapSqlParameterSource()
						.addValue("todoTabName", todoTabName)
						.addValue("userId", userId),
				keyHolder,
				new String[] { "todo_tab_id" });
		return keyHolder.getKey().longValue();
	}

	private long insertTodo(long todoTabId, String todoName, int completed, int total) {
		var keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				"INSERT INTO todos (todo_name, todo_tab_id, progress_completed, progress_total) VALUES (:todoName, :todoTabId, :completed, :total)",
				new MapSqlParameterSource()
						.addValue("todoName", todoName)
						.addValue("todoTabId", todoTabId)
						.addValue("completed", completed)
						.addValue("total", total),
				keyHolder,
				new String[] { "todo_id" });
		return keyHolder.getKey().longValue();
	}

	// ---- findTodoTabsByUserId ----

	@Test
	void 指定したuserIdのtodoTabsが取得できること() {
		insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");
		insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ２");
		insertTodoTab("550e8400-e29b-41d4-a716-446655440001", "タブ３");

		List<TodoTab> result = dao.findTodoTabsByUserId(new UserId("550e8400-e29b-41d4-a716-446655440000"));

		assertEquals(2, result.size());
		assertEquals("タブ１", result.get(0).getTodoTabName().value());
		assertEquals("タブ２", result.get(1).getTodoTabName().value());
	}

	@Test
	void todoTabが存在しないときは空リストが返ること() {
		List<TodoTab> result = dao.findTodoTabsByUserId(new UserId("550e8400-e29b-41d4-a716-446655440000"));

		assertTrue(result.isEmpty());
	}

	// ---- findTodosByUserIdAndTodoTabId ----

	@Test
	void 指定したtabIdのtodosが取得できること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");
		insertTodo(tabId, "タスク１", 0, 100);
		insertTodo(tabId, "タスク２", 1, 4);

		Optional<List<Todo>> result = dao.findTodosByUserIdAndTodoTabId(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(tabId));

		assertTrue(result.isPresent());
		assertEquals(2, result.get().size());
		assertEquals("タスク１", result.get().get(0).getTodoName().value());
		assertEquals(0, result.get().get(0).getCompleted());
		assertEquals(100, result.get().get(0).getTotal());
		assertEquals("タスク２", result.get().get(1).getTodoName().value());
		assertEquals(1, result.get().get(1).getCompleted());
		assertEquals(4, result.get().get(1).getTotal());
	}

	@Test
	void 指定したtabIdのtodoが0件のときは空リストが返ること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");

		Optional<List<Todo>> result = dao.findTodosByUserIdAndTodoTabId(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(tabId));

		assertTrue(result.isPresent());
		assertTrue(result.get().isEmpty());
	}

	@Test
	void 存在しないtabIdのtodosを取得すると空のOptionalが返ること() {
		Optional<List<Todo>> result = dao.findTodosByUserIdAndTodoTabId(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(1L));

		assertTrue(result.isEmpty());
	}

	// ---- createTodoTab ----

	@Test
	void todoTabが正常に作成されること() {
		TodoTabId result = dao.createTodoTab(new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabName.of("タブ１"));

		assertNotNull(result);
		assertTrue(result.value() > 0);
	}

	// ---- createTodo ----

	@Test
	void todoが正常に作成されること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");

		int result = dao.createTodo(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(tabId), TodoName.of("タスク１"));

		assertEquals(1, result);
	}

	@Test
	void 存在しないtabIdのtodoを作成すると0件が返ること() {
		int result = dao.createTodo(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(1L), TodoName.of("タスク１"));

		assertEquals(0, result);
	}

	// ---- updateTodoTab ----

	@Test
	void todoTabが正常に更新されること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");

		int result = dao.updateTodoTab(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(tabId), TodoTabName.of("タブ２"));

		assertEquals(1, result);
	}

	@Test
	void 存在しないtabIdのtodoTabを更新すると0件が返ること() {
		int result = dao.updateTodoTab(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(1L), TodoTabName.of("タブ１"));

		assertEquals(0, result);
	}

	// ---- updateTodo ----

	@Test
	void todoNameが正常に更新されること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");
		long todoId = insertTodo(tabId, "タスク１", 0, 100);

		int result = dao.updateTodo(
				new UserId("550e8400-e29b-41d4-a716-446655440000"),
				TodoTabId.of(tabId),
				TodoId.of(todoId),
				Optional.of(TodoName.of("タスク２")),
				Optional.empty());

		assertEquals(1, result);
	}

	@Test
	void progressRateが正常に更新されること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");
		long todoId = insertTodo(tabId, "タスク１", 0, 100);

		int result = dao.updateTodo(
				new UserId("550e8400-e29b-41d4-a716-446655440000"),
				TodoTabId.of(tabId),
				TodoId.of(todoId),
				Optional.empty(),
				Optional.of(ProgressRate.of(50, 200)));

		assertEquals(1, result);
	}

	@Test
	void 存在しないtodoIdのtodoを更新すると0件が返ること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");

		int result = dao.updateTodo(
				new UserId("550e8400-e29b-41d4-a716-446655440000"),
				TodoTabId.of(tabId),
				TodoId.of(1L),
				Optional.of(TodoName.of("タスク１")),
				Optional.empty());

		assertEquals(0, result);
	}

	@Test
	void 更新フィールドが空のときIllegalArgumentExceptionがスローされること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");
		long todoId = insertTodo(tabId, "タスク１", 0, 100);

		assertThrows(IllegalArgumentException.class, () -> dao.updateTodo(
				new UserId("550e8400-e29b-41d4-a716-446655440000"),
				TodoTabId.of(tabId),
				TodoId.of(todoId),
				Optional.empty(),
				Optional.empty()));
	}

	// ---- deleteTodoTabByUserIdAndTodoTabId ----

	@Test
	void todoTabが正常に削除されること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");

		int result = dao.deleteTodoTabByUserIdAndTodoTabId(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(tabId));

		assertEquals(1, result);
	}

	@Test
	void 存在しないtabIdのtodoTabを削除すると0件が返ること() {
		int result = dao.deleteTodoTabByUserIdAndTodoTabId(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(1L));

		assertEquals(0, result);
	}

	// ---- deleteTodoByUserIdAndTodoTabIdAndTodoId ----

	@Test
	void todoが正常に削除されること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");
		long todoId = insertTodo(tabId, "タスク１", 0, 100);

		int result = dao.deleteTodoByUserIdAndTodoTabIdAndTodoId(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(tabId), TodoId.of(todoId));

		assertEquals(1, result);
	}

	@Test
	void 存在しないtodoIdのtodoを削除すると0件が返ること() {
		long tabId = insertTodoTab("550e8400-e29b-41d4-a716-446655440000", "タブ１");

		int result = dao.deleteTodoByUserIdAndTodoTabIdAndTodoId(
				new UserId("550e8400-e29b-41d4-a716-446655440000"), TodoTabId.of(tabId), TodoId.of(1L));

		assertEquals(0, result);
	}

}
