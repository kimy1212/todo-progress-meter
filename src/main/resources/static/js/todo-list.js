/**
 * todoリスト画面
 */
import * as common from './common/common.js';
import * as editProgressRate from './edit-progress-rate.js';
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
		const savedTabId = sessionStorage.getItem('activeTabId');
		sessionStorage.removeItem('activeTabId');
		const savedTab = savedTabId
			? document.querySelector(`.todo-tab[data-todo-tab-id="${savedTabId}"]`)
			: null;
		activateTodoTab(savedTab ?? todoTabs[0]);
		loadTodosForActiveTodoTab();
	} else {
		document.getElementById('addTodoButton').style.display = 'none';
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
		if (event.target.tagName !== 'INPUT' || event.target.type !== 'text') return;
		if (event.key === 'Enter') {
			commitTodoTabName(event.target);
		} else if (event.key === 'Escape') {
			cancelTodoTabInput(event.target);
		}
	});

	todoTabList.addEventListener('dblclick', (event) => {
		if (event.target.tagName === 'SPAN') {
			common.replaceLabelWithInput(event.target, ['text-small-dark', 'todo-tab-text']);
		}
	});

	const addTodoTabButton = document.getElementById('addTodoTabButton');
	addTodoTabButton.addEventListener('click', handleAddTodoTabButtonClick);

	const sidebarToggleButton = document.getElementById('sidebarToggleButton');
	const todoTabListEl = document.getElementById('todoTabList');
	if (localStorage.getItem('sidebarCollapsed') === 'true') {
		todoTabListEl.classList.add('collapsed');
		sidebarToggleButton.querySelector('i').classList.replace('fa-angles-left', 'fa-angles-right');
	}
	sidebarToggleButton.addEventListener('click', () => {
		const isCollapsed = todoTabListEl.classList.toggle('collapsed');
		const icon = sidebarToggleButton.querySelector('i');
		icon.classList.toggle('fa-angles-left', !isCollapsed);
		icon.classList.toggle('fa-angles-right', isCollapsed);
		localStorage.setItem('sidebarCollapsed', isCollapsed);
		if (isCollapsed) {
			todoTabListEl.style.width = '';
		} else {
			const savedWidth = localStorage.getItem('sidebarWidth');
			if (savedWidth) todoTabListEl.style.width = savedWidth + 'px';
		}
	});

	const sidebarResizer = document.getElementById('sidebarResizer');
	initSidebarResizer(sidebarResizer, todoTabListEl);

	const todoList = document.getElementById('todoList');
	todoList.addEventListener('keydown', (event) => {
		if (event.target.tagName !== 'INPUT' || event.target.type !== 'text') return;
		if (event.key === 'Enter') {
			commitTodoName(event.target);
		} else if (event.key === 'Escape') {
			cancelTodoInput(event.target);
		}
	});

	todoList.addEventListener('dblclick', (event) => {
		if (event.target.tagName === 'LABEL') {
			const todo = event.target.closest('.todo');
			common.replaceLabelWithInput(event.target, ['text-large-dark', 'todo-text']);
			todo.querySelector('.todo-actions-trigger').style.display = 'none';
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
		const deleteTodoButton = event.target.closest('.delete-todo-button');
		if (!deleteTodoButton) return;
		const todoId = event.target.closest('.todo').dataset.todoId;
		handleDeleteTodoButtonClick(getActiveTodoTabId(), todoId);
	});

	const addTodoButton = document.getElementById('addTodoButton');
	addTodoButton.addEventListener('click', handleAddTodoButtonClick);

	//進捗率編集画面
	todoList.addEventListener('click', (event) => {
		const editButton = event.target.closest('.edit-progress-rate-button');
		if (!editButton) return;
		const todo = editButton.closest('.todo');
		editProgressRate.init(getActiveTodoTabId(), todo.dataset.todoId, Number(todo.dataset.completed), Number(todo.dataset.total));
	});

}

/**
 * サイドバーリサイザー初期化
 *
 * @param {HTMLElement} resizer リサイザー要素
 * @param {HTMLElement} sidebar サイドバー要素
 */
const MIN_SIDEBAR_WIDTH = 230;
const MAX_SIDEBAR_WIDTH = 700;

function initSidebarResizer(resizer, sidebar) {
	const savedWidth = localStorage.getItem('sidebarWidth');
	if (savedWidth && !sidebar.classList.contains('collapsed')) {
		sidebar.style.width = savedWidth + 'px';
	}

	resizer.addEventListener('mousedown', (e) => {
		if (sidebar.classList.contains('collapsed')) return;

		e.preventDefault();
		resizer.classList.add('is-dragging');
		sidebar.classList.add('is-dragging');
		document.body.classList.add('todo-list-screen-no-select');

		const onMouseMove = (e) => {
			const newWidth = e.clientX - sidebar.getBoundingClientRect().left;
			if (newWidth < MIN_SIDEBAR_WIDTH || newWidth > MAX_SIDEBAR_WIDTH) return;
			sidebar.style.width = newWidth + 'px';
		};

		const onMouseUp = () => {
			resizer.classList.remove('is-dragging');
			sidebar.classList.remove('is-dragging');
			document.body.classList.remove('todo-list-screen-no-select');
			localStorage.setItem('sidebarWidth', parseInt(sidebar.style.width));
			document.removeEventListener('mousemove', onMouseMove);
			document.removeEventListener('mouseup', onMouseUp);
		};

		document.addEventListener('mousemove', onMouseMove);
		document.addEventListener('mouseup', onMouseUp);
	});
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

	const existingTabNames = [...document.querySelectorAll('.todo-tab-label')]
		.map(el => el.textContent);
	const result = validateTodoTabName(todoTabName, existingTabNames);
	if (result !== TodoTabNameValidationResult.OK) {
		common.showInputError(input.parentNode, todoTabNameErrorMessage(result), input);
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

		common.clearInputError(input.parentNode);
		common.replaceInputWithLabel(input, 'span', ['text-small-dark', 'todo-tab-label']);

		const todoTab = document.querySelector(`.todo-tab[data-todo-tab-id="${todoTabId}"]`);
		activateTodoTab(todoTab);
		loadTodosForActiveTodoTab();
	} catch {
		common.showInputError(input.parentNode, '保存に失敗しました', input);
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

	const existingTodoNames = [...document.querySelectorAll('#todoList .todo-content label')]
		.map(el => el.textContent);
	const result = validateTodoName(todoName, existingTodoNames);
	if (result !== TodoNameValidationResult.OK) {
		common.showInputError(input.parentNode, todoNameErrorMessage(result), input);
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

		common.clearInputError(input.parentNode);
		common.replaceInputWithLabel(input, 'label', 'text-large-dark');

		loadTodosForActiveTodoTab();
	} catch {
		common.showInputError(input.parentNode, '保存に失敗しました', input);
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
	if (res === undefined) return;
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
	const activeTabId = getActiveTodoTabId();
	if (activeTabId !== tabId) {
		sessionStorage.setItem('activeTabId', activeTabId);
	}
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
	const todoTabScrollArea = document.getElementById('todoTabScrollArea');

	const newTodoTab = document.createElement('div');
	newTodoTab.classList.add('todo-tab');

	const newDeleteTodoTabButton = document.createElement('button');
	newDeleteTodoTabButton.classList.add('delete-todo-tab-button');

	const newDeleteTodoTabButtonIcon = document.createElement('i');
	newDeleteTodoTabButtonIcon.classList.add('fa-solid', 'fa-trash');

	newDeleteTodoTabButton.append(newDeleteTodoTabButtonIcon);
	newTodoTab.append(common.createInputText('', ['text-small-dark', 'todo-tab-text']), newDeleteTodoTabButton);
	todoTabScrollArea.appendChild(newTodoTab);

	//スクロールバーを最下部に移動
	const bottom = todoTabScrollArea.scrollHeight - todoTabScrollArea.clientHeight;
	todoTabScrollArea.scroll(0, bottom);
}

/**
 * todoタブ入力キャンセル処理
 *
 * @param {HTMLElement} input todoタブのinput要素
 */
function cancelTodoTabInput(input) {
	const todoTab = input.closest('.todo-tab');
	if (todoTab.dataset.todoTabId) {
		common.clearInputError(input.parentNode);
		input.value = input.dataset.originalValue;
		common.replaceInputWithLabel(input, 'span', ['text-small-dark', 'todo-tab-label']);
	} else {
		todoTab.remove();
	}
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

	const todoElement = todoClone.querySelector('.todo');
	todoElement.dataset.todoId = todo.todoId;
	todoElement.dataset.completed = todo.completed ?? 0;
	todoElement.dataset.total = todo.total ?? 0;

	if (mode === 'span' || mode === 'label') {
		todoContent.append(common.createLabel(mode, todo.todoName, 'text-large-dark'));
	} else if (mode === 'input') {
		todoContent.append(common.createInputText(todo.todoName, ['text-large-dark', 'todo-text']));
		todoClone.querySelector('.todo-actions-trigger').style.display = 'none';
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
			const clamped = Math.min(100, Math.max(0, todo.progressRate ?? 0));
			insertedTodo.querySelector('.donut-chart').style.setProperty('--value', clamped);
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

	//スクロールバーを最下部に移動
	const bottom = todoList.scrollHeight - todoList.clientHeight;
	todoList.scroll(0, bottom);
}

/**
 * todo入力キャンセル処理
 *
 * @param {HTMLElement} input todoのinput要素
 */
function cancelTodoInput(input) {
	const todo = input.closest('.todo');
	if (todo.dataset.todoId) {
		common.clearInputError(input.parentNode);
		input.value = input.dataset.originalValue;
		common.replaceInputWithLabel(input, 'label', 'text-large-dark');
		todo.querySelector('.todo-actions-trigger').style.display = '';
	} else {
		todo.remove();
	}
}
