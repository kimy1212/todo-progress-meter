package com.kimy1212.progressmeter.presentation.controller.todo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import com.kimy1212.progressmeter.application.command.GetTodoTabsCommand;
import com.kimy1212.progressmeter.application.response.TodoTabDto;
import com.kimy1212.progressmeter.application.service.todo.TodoService;
import com.kimy1212.progressmeter.presentation.dto.response.TodoTabResponse;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TodoService service;

	@MockitoBean
	private ClientRegistrationRepository clientRegistrationRepository;

	@Test
	void 未認証でトップ画面にアクセスするとログイン画面にリダイレクトされること() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	void トップ画面でtodoTabsがModelに設定されること() throws Exception {
		List<TodoTabDto> dtos = List.of(
				new TodoTabDto(1, "タブ１"),
				new TodoTabDto(2, "タブ２"));

		when(service.getTodoTabs(any(GetTodoTabsCommand.class)))
				.thenReturn(dtos);

		MvcResult result = mockMvc.perform(get("/")
				.with(oauth2Login().attributes(attrs -> attrs.put("sub", "test-sub"))))
				.andExpect(status().isOk())
				.andExpect(view().name("todo-list"))
				.andReturn();

		ModelAndView mav = result.getModelAndView();
		Object modelObject = mav.getModel().get("todoTabs");
		assertNotNull(modelObject);

		@SuppressWarnings("unchecked")
		List<TodoTabResponse> todoTabs = (List<TodoTabResponse>) modelObject;

		assertEquals(2, todoTabs.size());

		TodoTabResponse first = todoTabs.get(0);
		assertEquals(1, first.todoTabId());
		assertEquals("タブ１", first.todoTabName());

		TodoTabResponse second = todoTabs.get(1);
		assertEquals(2, second.todoTabId());
		assertEquals("タブ２", second.todoTabName());

	}

}
