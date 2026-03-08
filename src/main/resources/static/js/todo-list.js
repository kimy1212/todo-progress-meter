/**
 * todoリスト画面
 */
import * as common from './common/common.js';
import { TodoTabNameValidationResult, validateTodoTabName } from './validation/todoTabName/validateTodoTabName.js';
import { todoTabNameErrorMessage } from './validation/todoTabName/todoTabNameErrorMessage.js';
import { TodoNameValidationResult, validateTodoName } from './validation/todoName/validateTodoName.js';
import { todoNameErrorMessage } from './validation/todoName/todoNameErrorMessage.js';

/**
 * 初期表示
 */
document.addEventListener('DOMContentLoaded', init);

function init() {

	const todoTabs = document.querySelectorAll('.todo-tab');
	if (todoTabs.length > 0) {
		activateTodoTab(todoTabs[0]);
		loadTodosForActiveTodoTab();
	}

	//イベント付与
	const todoTabList = document.getElementById('todoTabList');
	todoTabList.addEventListener('click', (event) => {
		const deleteTodoTabButton = event.target.closest('.delete-todo-tab-button');
		if (deleteTodoTabButton) {
			const todoTab = deleteTodoTabButton.closest('.todo-tab');
			if (!todoTab) return;

			handleDeleteTodoTabButtonClick(todoTab.dataset.todoTabId);
			return;
		}

		const todoTab = event.target.closest('.todo-tab');
		if (!todoTab) return;
		handleTodoTabClick(todoTab);
	});

	todoTabList.addEventListener('keydown', (event) => {
		if (event.key === 'Enter' && event.target.tagName === 'INPUT' && event.target.type === 'text') {
			commitTodoTabName(event.target);
		}
	});

	todoTabList.addEventListener('dblclick', (event) => {
		if (event.target.tagName === 'SPAN') {
			common.replaceLabelWithInput(event.target, ['text-small-dark', 'todo-tab-text']);
		}
	});

	const addTodoTabButton = document.getElementById('addTodoTabButton');
	addTodoTabButton.addEventListener('click', handleAddTodoTabButtonClick);

	const todoList = document.getElementById('todoList');
	todoList.addEventListener('keydown', (event) => {
		if (event.key === 'Enter' && event.target.tagName === 'INPUT' && event.target.type === 'text') {
			commitTodoName(event.target);
		}
	});

	todoList.addEventListener('dblclick', (event) => {
		if (event.target.tagName === 'LABEL') {
			common.replaceLabelWithInput(event.target, ['text-large-dark', 'todo-text']);
		}
	});

	document.addEventListener('click', (event) => {
		const trigger = event.target.closest('.todo-actions-trigger');
		const menu = event.target.closest('.todo-actions-menu');

		if (trigger) {
			const targetMenu = trigger.nextElementSibling;
			targetMenu.classList.toggle('is-open');
			return;
		}

		if (menu) return;

		document
			.querySelectorAll('.todo-actions-menu.is-open')
			.forEach(e => e.classList.remove('is-open'));
	});

	todoList.addEventListener('click', (event) => {
		const deleteTodoButton = event.target.closest('#deleteTodoButton');
		if (!deleteTodoButton) return;
		const todoId = event.target.closest('.todo').dataset.todoId;
		handleDeleteTodoButtonClick(getActiveTodoTabId(), todoId);
	});

	const addTodoButton = document.getElementById('addTodoButton');
	addTodoButton.addEventListener('click', handleAddTodoButtonClick);
}

/**
 * todo取得
 * 
 * @param {number} tabId 選択したtodoタブのtodoタブID
 */
let todosController;

async function fetchTodos(tabId) {
	if (todosController) {
		todosController.abort();
	}

	todosController = new AbortController();

	try {
		const res = await fetch(`/api/tabs/${tabId}/todos`, {
			method: 'GET',
			signal: todosController.signal
		});

		if (!res.ok) {
			common.redirectByStatusCode(res.status);
			return;
		}

		return await res.json();

	} catch (e) {
		if (e.name === 'AbortError') {
			return;
		}

		common.redirectToGenericError();
	} finally {
		todosController = null;
	}
}

/**
 * todoタブ作成
 * 
 * @param {string} todoTabName 作成対象のtodoタブ名
 */
async function createTodoTab(todoTabName) {
	try {
		const dto = {
			todoTabName: todoTabName.trim(),
		};

		const res = await fetch(`/api/tabs`, {
			method: 'POST',
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(dto),
		});

		if (!res.ok) {
			common.redirectByStatusCode(res.status);
			return false;
		}

		return await res.json();
	} catch {
		common.redirectToGenericError();
		return false;
	}
}

/**
 * todo作成
 * 
 * @param {number} tabId 作成対象のtodoに紐づくタブID
 * @param {string} todoName 作成対象のtodo名
 */
async function createTodo(tabId, todoName) {
	try {
		const dto = {
			todoName: todoName.trim(),
		};

		const res = await fetch(`/api/tabs/${tabId}/todos`, {
			method: 'POST',
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(dto),
		});

		if (!res.ok) {
			common.redirectByStatusCode(res.status);
			return false;
		}

		return true;
	} catch {
		common.redirectToGenericError();
		return false;
	}
}

/**
 * todoタブ名更新
 * 
 * @param {number} tabId 更新対象のタブID
 * @param {string} todoTabName 更新対象のtodoタブ名
 */
async function updateTodoTabName(tabId, todoTabName) {
	try {
		const dto = {
			todoTabName: todoTabName.trim(),
		};

		const res = await fetch(`/api/tabs/${tabId}`, {
			method: 'PATCH',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(dto),
		});

		if (!res.ok) {
			common.redirectByStatusCode(res.status);
			return false;
		}

		return true;
	} catch {
		common.redirectToGenericError();
		return false;
	}
}

/**
 * todo名更新
 * 
 * @param {number} tabId 更新対象のtodoに紐づくタブID
 * @param {number} todoId 更新対象のtodoID
 * @param {string} todoName 更新対象のtodo名
 */
async function updateTodoName(tabId, todoId, todoName) {
	try {
		const dto = {
			todoName: todoName.trim(),
		}

		const res = await fetch(`/api/tabs/${tabId}/todos/${todoId}`, {
			method: 'PATCH',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(dto),
		});

		if (!res.ok) {
			common.redirectByStatusCode(res.status);
			return false;
		}

		return true;
	} catch {
		common.redirectToGenericError();
		return false;
	}
}

/**
 * todoタブ削除
 *
 * @param {number} tabId 削除対象のtodoタブID
 */
async function deleteTodoTab(tabId) {
	try {
		const res = await fetch(`/api/tabs/${tabId}`, {
			method: 'DELETE',
		});

		if (!res.ok) {
			common.redirectByStatusCode(res.status);
			return false;
		}

		return true;
	} catch {
		common.redirectToGenericError();
		return false;
	}
}

/**
 * todo削除
 *
 * @param {number} tabId 削除対象のtodoタブID
 * @param {number} todoId 削除対象のtodoID
 */
async function deleteTodo(tabId, todoId) {
	try {
		const res = await fetch(`/api/tabs/${tabId}/todos/${todoId}`, {
			method: 'DELETE',
		});

		if (!res.ok) {
			common.redirectByStatusCode(res.status);
			return false;
		}

		return true;
	} catch {
		common.redirectToGenericError();
		return false;
	}
}

/**
 * todoタブ名確定
 * 
 * @param {HTMLElement} input todoタブ要素
 */
async function commitTodoTabName(input) {
	let todoTabId = input.closest('.todo-tab').dataset.todoTabId;
	const todoTabName = input.value;

	const result = validateTodoTabName(todoTabName);
	if (result !== TodoTabNameValidationResult.OK) {
		alert(todoTabNameErrorMessage(result));
		input.focus();
		return;
	}

	try {
		if (todoTabId) {
			await updateTodoTabName(todoTabId, todoTabName);
		} else {
			const data = await createTodoTab(todoTabName);
			todoTabId = data.todoTabId;
			input.closest('.todo-tab').dataset.todoTabId = todoTabId;
		}

		common.replaceInputWithLabel(input, 'span', ['text-small-dark', 'todo-tab-label']);

		const todoTab = document.querySelector(`.todo-tab[data-todo-tab-id="${todoTabId}"]`);
		activateTodoTab(todoTab);
		loadTodosForActiveTodoTab();
	} catch {
		alert('保存に失敗しました');
		input.focus();
	}
}

/**
 * todo名確定
 * 
 * @param {HTMLElement} input todo要素
 */
async function commitTodoName(input) {
	let todoId = input.closest('.todo').dataset.todoId;
	const todoName = input.value;

	const result = validateTodoName(todoName);
	if (result !== TodoNameValidationResult.OK) {
		alert(todoNameErrorMessage(result));
		input.focus();
		return;
	}

	try {
		if (todoId) {
			await updateTodoName(getActiveTodoTabId(), todoId, todoName);
		} else {
			const isCreated = await createTodo(getActiveTodoTabId(), todoName);
			if (!isCreated) return;
		}

		common.replaceInputWithLabel(input, 'label', 'text-large-dark');

		loadTodosForActiveTodoTab();
	} catch {
		alert('保存に失敗しました');
		input.focus();
	}
}

/**
 * todoタブ押下
 * 
 * @param {HTMLElement} todoTab todoタブ要素
 */
function handleTodoTabClick(todoTab) {
	activateTodoTab(todoTab);
	loadTodosForActiveTodoTab();
}

/**
 * todoタブアクティブ化処理
 * 
 * @param {HTMLElement} activeTodoTab アクティブにするtodoタブ要素
 */
function activateTodoTab(activeTodoTab) {
	if (activeTodoTab.querySelector('input')) return;

	document.querySelectorAll('.todo-tab.active').forEach(todoTab => {
		todoTab.classList.remove('active');
	});
	activeTodoTab.classList.add('active');
}

/**
 * アクティブtodoタブのtodo取得処理
 */
async function loadTodosForActiveTodoTab() {
	const activeTodoTabId = document.querySelector('.todo-tab.active').dataset.todoTabId;
	const res = await fetchTodos(activeTodoTabId);
	createTodoList(res);
}

/**
 * アクティブtodoタブID取得処理
 * 
 * @return {number} アクティブtodoタブのtodoタブID
 */
function getActiveTodoTabId() {
	return document.querySelector('.todo-tab.active').dataset.todoTabId;
}

/**
 * todoタブ削除ボタン押下
 * 
 * @param {number} tabId 削除対象のtodoタブID
 */
async function handleDeleteTodoTabButtonClick(tabId) {
	const isDeleted = await deleteTodoTab(tabId);
	if (!isDeleted) return;
	window.location.href = '/';
}

/**
 * todoタブ追加ボタン押下
 */
function handleAddTodoTabButtonClick() {
	addTodoTab();
}

/**
 * todoタブ追加処理
 */
function addTodoTab() {
	const todoTabList = document.getElementById('todoTabList');
	const addTodoTabButton = document.getElementById('addTodoTabButton');

	const newTodoTab = document.createElement('div');
	newTodoTab.classList.add('todo-tab');

	const newDeleteTodoTabButton = document.createElement('button');
	newDeleteTodoTabButton.classList.add('delete-todo-tab-button');

	const newDeleteTodoTabButtonIcon = document.createElement('i');
	newDeleteTodoTabButtonIcon.classList.add('fa-solid', 'fa-trash');

	newDeleteTodoTabButton.append(newDeleteTodoTabButtonIcon);
	newTodoTab.append(common.createInputText('', ['text-small-dark', 'todo-tab-text']), newDeleteTodoTabButton);
	todoTabList.insertBefore(newTodoTab, addTodoTabButton);
}

/**
 * todo要素生成
 * 
 * @param {'span' | 'label' | 'input'} mode 生成タグ
 * @param {{todoId: number, todoName: string}[]} todo 生成対象のtodo
 * @return {HTMLElement} todo要素
 */
function createTodoElement(mode, todo) {
	const todoTemplate = document.getElementById('todo');

	const todoClone = todoTemplate.content.cloneNode(true);

	const todoContent = todoClone.querySelector('.todo-content');

	todoClone.querySelector('.todo').dataset.todoId = todo.todoId;

	if (mode === 'span' || mode === 'label') {
		todoContent.append(common.createLabel(mode, todo.todoName, 'text-large-dark'));
	} else if (mode === 'input') {
		todoContent.append(common.createInputText(todo.todoName, ['text-large-dark', 'todo-text']));
	}

	todoClone.querySelector('.progress-rate-value').textContent = todo.progressRate ?? 0;

	return todoClone;
}

/**
 * todoリスト作成
 * 
 * @param {{todoId: number, todoName: string}[]} todos 取得したtodo
 */
function createTodoList(todos) {
	document.querySelectorAll('#todoList .todo').forEach(e => e.remove());

	todos.forEach(todo => {
		displayTodo(todo);
	});
}

/**
 * todo表示処理（初期表示用）
 * 
 * @param {{todoId: number, todoName: string}[]} todo 表示対象のtodo
 */
function displayTodo(todo) {
	const todoList = document.getElementById('todoList');
	const addTodoButton = document.getElementById('addTodoButton');

	const newTodo = createTodoElement('label', todo);
	todoList.insertBefore(newTodo, addTodoButton);

	requestAnimationFrame(() => {
		requestAnimationFrame(() => {
			const insertedTodo = todoList.querySelector(`.todo[data-todo-id="${todo.todoId}"]`);
			insertedTodo.querySelector('.donut-chart').style.setProperty('--value', todo.progressRate ?? 0);
		});
	});
}

/**
 * todo削除ボタン押下
 * 
 * @param {number} tabId 削除対象のtodoタブID
 * @param {number} todoId 削除対象のtodoID
 */
async function handleDeleteTodoButtonClick(tabId, todoId) {
	const isDeleted = await deleteTodo(tabId, todoId);
	if (!isDeleted) return;
	loadTodosForActiveTodoTab();
}

/**
 * todo追加ボタン押下
 */
function handleAddTodoButtonClick() {
	addTodo();
}

/**
 * todo追加処理（todo追加ボタン押下時）
 */
function addTodo() {
	const todoList = document.getElementById('todoList');
	const addTodoButton = document.getElementById('addTodoButton');

	const newTodoData = {
		todoId: '',
		todoName: '',
		progressRate: 0
	}
	const newTodo = createTodoElement('input', newTodoData);
	todoList.insertBefore(newTodo, addTodoButton);
}
