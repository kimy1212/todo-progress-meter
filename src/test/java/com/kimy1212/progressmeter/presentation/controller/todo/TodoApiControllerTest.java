package com.kimy1212.progressmeter.presentation.controller.todo;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.OAuth2LoginRequestPostProcessor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.kimy1212.progressmeter.application.exception.NotFoundException;
import com.kimy1212.progressmeter.application.response.TodoDto;
import com.kimy1212.progressmeter.application.service.todo.TodoService;
import com.kimy1212.progressmeter.domain.model.TodoTabId;

@WebMvcTest(TodoApiController.class)
class TodoApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TodoService service;

	@MockitoBean
	private ClientRegistrationRepository clientRegistrationRepository;

	private OAuth2LoginRequestPostProcessor loggedIn() {
		return oauth2Login().attributes(attrs -> attrs.put("sub", "test-sub"));
	}

	// ---- GET /api/tabs/{tabId}/todos ----

	@Test
	void 未認証でtodo一覧を取得するとログイン画面にリダイレクトされること() throws Exception {
		mockMvc.perform(get("/api/tabs/1/todos"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	void todo一覧を正常に取得できること() throws Exception {
		List<TodoDto> dtos = List.of(new TodoDto(1L, "タスク１", 50, 1, 2));
		when(service.getTodos(any())).thenReturn(dtos);

		mockMvc.perform(get("/api/tabs/1/todos")
				.with(loggedIn())
				.header("X-Requested-With", "XMLHttpRequest"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].todoId").value(1))
				.andExpect(jsonPath("$[0].todoName").value("タスク１"))
				.andExpect(jsonPath("$[0].progressRate").value(50));
	}

	@Test
	void 存在しないTabIdでtodo一覧を取得すると404が返ること() throws Exception {
		when(service.getTodos(any())).thenThrow(new NotFoundException("Todo tab not found"));

		mockMvc.perform(get("/api/tabs/1/todos")
				.with(loggedIn())
				.header("X-Requested-With", "XMLHttpRequest"))
				.andExpect(status().isNotFound());
	}

	@Test
	void tabIdが0のときtodo一覧取得で400が返ること() throws Exception {
		mockMvc.perform(get("/api/tabs/0/todos")
				.with(loggedIn())
				.header("X-Requested-With", "XMLHttpRequest"))
				.andExpect(status().isBadRequest());
	}

	// ---- POST /api/tabs ----

	@Test
	void todoTabを正常に作成できること() throws Exception {
		when(service.createTodoTab(any())).thenReturn(TodoTabId.of(1L));

		mockMvc.perform(post("/api/tabs")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoTabName": "タブ１" }
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.todoTabId").value(1));
	}

	@Test
	void todoTabNameが空白のときtodoTab作成で400が返ること() throws Exception {
		mockMvc.perform(post("/api/tabs")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoTabName": "" }
						"""))
				.andExpect(status().isBadRequest());
	}

	// ---- POST /api/tabs/{tabId}/todos ----

	@Test
	void todoを正常に作成できること() throws Exception {
		mockMvc.perform(post("/api/tabs/1/todos")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoName": "タスク１" }
						"""))
				.andExpect(status().isOk());

		verify(service).createTodo(any());
	}

	@Test
	void 存在しないtabIdでtodoを作成すると404が返ること() throws Exception {
		doThrow(new NotFoundException("Todo tab not found")).when(service).createTodo(any());

		mockMvc.perform(post("/api/tabs/1/todos")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoName": "タスク１" }
						"""))
				.andExpect(status().isNotFound());
	}

	@Test
	void todoNameが空白のときtodo作成で400が返ること() throws Exception {
		mockMvc.perform(post("/api/tabs/1/todos")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoName": "" }
						"""))
				.andExpect(status().isBadRequest());
	}

	//---- PATCH /api/tabs/{tabId} ----

	@Test
	void todoTabを正常に更新できること() throws Exception {
		mockMvc.perform(patch("/api/tabs/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoTabName": "タブ１" }
						"""))
				.andExpect(status().isOk());

		verify(service).updateTodoTab(any());
	}

	@Test
	void 存在しないtabIdのtodoTabを更新すると404が返ること() throws Exception {
		doThrow(new NotFoundException("Todo tab not found")).when(service).updateTodoTab(any());

		mockMvc.perform(patch("/api/tabs/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoTabName": "タブ１" }
						"""))
				.andExpect(status().isNotFound());
	}

	@Test
	void todoTabNameが空白のときtodoTab更新で400が返ること() throws Exception {
		mockMvc.perform(patch("/api/tabs/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoTabName": "" }
						"""))
				.andExpect(status().isBadRequest());
	}

	// ---- PATCH /api/tabs/{tabId}/todos/{todoId} ----

	@Test
	void todoNameを正常に更新できること() throws Exception {
		mockMvc.perform(patch("/api/tabs/1/todos/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoName": "タスク１" }
						"""))
				.andExpect(status().isOk());

		verify(service).updateTodo(any());
	}

	@Test
	void progressRateを正常に更新できること() throws Exception {
		mockMvc.perform(patch("/api/tabs/1/todos/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "completed": 50, "total": 100 }
						"""))
				.andExpect(status().isOk());

		verify(service).updateTodo(any());
	}

	@Test
	void 更新項目が空のときtodo更新で400が返ること() throws Exception {
		mockMvc.perform(patch("/api/tabs/1/todos/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{}
						"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void completedがtotalより大きいときtodo更新で400が返ること() throws Exception {
		mockMvc.perform(patch("/api/tabs/1/todos/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "completed": 101, "total": 100 }
						"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void completedとtotalが等しいときtodo更新で200が返ること() throws Exception {
		mockMvc.perform(patch("/api/tabs/1/todos/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "completed": 100, "total": 100 }
						"""))
				.andExpect(status().isOk());

		verify(service).updateTodo(any());
	}

	@Test
	void 存在しないtodoIdのtodoを更新すると404が返ること() throws Exception {
		doThrow(new NotFoundException("Todo not found")).when(service).updateTodo(any());

		mockMvc.perform(patch("/api/tabs/1/todos/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{ "todoName": "タスク１" }
						"""))
				.andExpect(status().isNotFound());
	}

	// ---- DELETE /api/tabs/{tabId} ----

	@Test
	void todoTabを正常に削除できること() throws Exception {
		mockMvc.perform(delete("/api/tabs/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest"))
				.andExpect(status().isOk());

		verify(service).deleteTodoTab(any());
	}

	@Test
	void 存在しないtabIdのtodoTabを削除すると404が返ること() throws Exception {
		doThrow(new NotFoundException("Todo tab not found")).when(service).deleteTodoTab(any());

		mockMvc.perform(delete("/api/tabs/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest"))
				.andExpect(status().isNotFound());
	}

	// ---- DELETE /api/tabs/{tabId}/todos/{todoId} ----

	@Test
	void todoを正常に削除できること() throws Exception {
		mockMvc.perform(delete("/api/tabs/1/todos/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest"))
				.andExpect(status().isOk());

		verify(service).deleteTodo(any());
	}

	@Test
	void 存在しないtodoIdのtodoを削除すると404が返ること() throws Exception {
		doThrow(new NotFoundException("Todo not found")).when(service).deleteTodo(any());

		mockMvc.perform(delete("/api/tabs/1/todos/1")
				.with(loggedIn()).with(csrf())
				.header("X-Requested-With", "XMLHttpRequest"))
				.andExpect(status().isNotFound());
	}

}
