/**
 * todoリスト画面
 */
import * as common from './common/common.js';

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
			common.replaceInputWithLabel(event.target, 'span', ['text-small-dark', 'todo-tab-label']);
		}
	});

	todoTabList.addEventListener('focusout', (event) => {
		if (event.target.tagName === 'INPUT' && event.target.type === 'text') {
			common.replaceInputWithLabel(event.target, 'span', ['text-small-dark', 'todo-tab-label']);
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
			common.replaceInputWithLabel(event.target, 'label', 'text-large-dark');
		}
	});

	todoList.addEventListener('focusout', (event) => {
		if (event.target.tagName === 'INPUT' && event.target.type === 'text') {
			common.replaceInputWithLabel(event.target, 'label', 'text-large-dark');
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

	newTodoTab.append(common.createInputText('', ['text-small-dark', 'todo-tab-text']));
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

	const newTodo = createTodoElement('input', '');
	todoList.insertBefore(newTodo, addTodoButton);
}
