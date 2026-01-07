package com.kimy1212.progressmeter.controller.todo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import com.kimy1212.progressmeter.controller.dto.TodoTabResponse;
import com.kimy1212.progressmeter.domain.valueobject.UserId;
import com.kimy1212.progressmeter.infrastructure.repository.row.TodoTabRow;
import com.kimy1212.progressmeter.service.todo.TodoService;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TodoService service;

	@Test
	void トップ画面でtodoTabsがModelに設定されること() throws Exception {
		List<TodoTabRow> rows = List.of(
				new TodoTabRow(1, "仕事"),
				new TodoTabRow(2, "プライベート"));

		when(service.getTodoTabs(any(UserId.class)))
				.thenReturn(rows);

		MvcResult result = mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("todo-list"))
				.andReturn();
		
		ModelAndView mav = result.getModelAndView();
		assertNotNull(mav);
		
		Object modelObject = mav.getModel().get("todoTabs");
		assertNotNull(modelObject);
		
		@SuppressWarnings("unchecked")
		List<TodoTabResponse> todoTabs = (List<TodoTabResponse>) modelObject;
		
		assertEquals(2, todoTabs.size());
		
		TodoTabResponse first = todoTabs.get(0);
		assertEquals(1, first.todoTabId());
		assertEquals("仕事", first.todoTabName());
		
		TodoTabResponse second = todoTabs.get(1);
		assertEquals(2, second.todoTabId());
		assertEquals("プライベート", second.todoTabName());

	}

}
