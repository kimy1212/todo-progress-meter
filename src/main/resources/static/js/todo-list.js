/**
 * todoリスト画面
 */
import * as common from './common/common.js';

/**
 * 初期表示
 */
document.addEventListener('DOMContentLoaded', init);

async function init() {

	const tabs = document.querySelectorAll('.todo-tab');
	if (tabs.length > 0) {
		tabs[0].classList.add('active');
		const activeTabId = document.querySelector('.todo-tab.active').dataset.tabId;
		const res = await fetchTodos(activeTabId);
		createTodoList(res.todos);
	}

	//イベント付与
	const todoTabs = document.getElementById('todoTabs');
	todoTabs.addEventListener('keydown', (event) => {
		if (event.key === 'Enter' && event.target.tagName === 'INPUT' && event.target.type === 'text') {
			common.replaceInputWithLabel(event.target, 'span', ['text-small-dark', 'todo-tab-label']);
		}
	});

	todoTabs.addEventListener('focusout', (event) => {
		if (event.target.tagName === 'INPUT' && event.target.type === 'text') {
			common.replaceInputWithLabel(event.target, 'span', ['text-small-dark', 'todo-tab-label']);
		}
	});

	todoTabs.addEventListener('dblclick', (event) => {
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
 * @param {number} tabId 選択したタブのタブID
 */
async function fetchTodos(tabId) {
	const controller = new AbortController();

	try {
		const res = await fetch(`/api/todo-list/${tabId}`, {
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
 * todoタブ追加ボタン押下
 */
function handleAddTodoTabButtonClick() {
	addTodoTab();
}

/**
 * todoタブ追加処理
 */
function addTodoTab() {
	const todoTabs = document.getElementById('todoTabs');
	const addTodoTabButton = document.getElementById('addTodoTabButton');

	const newTodoTab = document.createElement('div');
	newTodoTab.classList.add('todo-tab');

	newTodoTab.append(common.createInputText('', ['text-small-dark', 'todo-tab-text']));
	todoTabs.insertBefore(newTodoTab, addTodoTabButton);
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
 * @param {{todoId: number, todoName: string}[]} todos 初期表示時に取得したtodo
 */
function createTodoList(todos) {
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