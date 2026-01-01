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

	const addTodoButton = document.getElementById('addTodoButton');
	addTodoButton.addEventListener('click', handleAddTodoButtonClick);
}

/**
 * todo取得
 * 
 * @param {number} tabId 選択したtodoタブのtodoタブID
 */
async function fetchTodos(tabId) {
	const controller = new AbortController();

	try {
		const res = await fetch(`/api/tabs/${tabId}/todos`, {
			method: 'GET',
			signal: controller.signal
		});

		if (!res.ok) {
			throw new Error(`Failed to fetch todos: status ${res.status}`);
		}

		return await res.json();

	} catch (e) {
		console.error(e);
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
 * @param {string} todoName todo名
 * @return {HTMLElement} todo要素
 */
function createTodoElement(mode, todoName) {
	const newTodo = document.createElement('div');
	newTodo.classList.add('todo');

	const newCheckbox = document.createElement('input');
	newCheckbox.type = 'checkbox';
	newCheckbox.classList.add('todo-checkbox');
	newCheckbox.name = 'todo';

	if (mode === 'span' || mode === 'label') {
		newTodo.append(newCheckbox, common.createLabel(mode, todoName, 'text-large-dark'));
	} else if (mode === 'input') {
		newTodo.append(newCheckbox, common.createInputText(todoName, ['text-large-dark', 'todo-text']));
	}

	return newTodo;
}

/**
 * todoリスト作成
 * 
 * @param {{todoId: number, todoName: string}[]} todos 取得したtodo
 */
function createTodoList(todos) {
	document.querySelectorAll('#todoList .todo').forEach(e => e.remove());
	
	todos.forEach(todo => {
		displayTodo(todo.todoName);
	});
}

/**
 * todo表示処理（初期表示用）
 * 
 * @param {string} todoName todo名
 */
function displayTodo(todoName) {
	const todoList = document.getElementById('todoList');
	const addTodoButton = document.getElementById('addTodoButton');

	const newTodo = createTodoElement('label', todoName);
	todoList.insertBefore(newTodo, addTodoButton);
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