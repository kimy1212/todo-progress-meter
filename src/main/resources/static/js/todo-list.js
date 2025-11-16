/**
 * todoリスト画面
 */
import * as common from './common/common.js';

/**
 * 初期表示
 */
document.addEventListener('DOMContentLoaded', init);

function init() {

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
 * todo追加ボタン押下
 */
function handleAddTodoButtonClick() {
	addTodo();
}

/**
 * todo追加処理
 */
function addTodo() {
	const todoList = document.getElementById('todoList');
	const addTodoButton = document.getElementById('addTodoButton');

	const newTodo = document.createElement('div');
	newTodo.classList.add('todo');

	const newCheckbox = document.createElement('input');
	newCheckbox.type = 'checkbox';
	newCheckbox.classList.add('todo-checkbox');
	newCheckbox.name = 'todo';

	newTodo.append(newCheckbox, common.createInputText('', ['text-large-dark', 'todo-text']));
	todoList.insertBefore(newTodo, addTodoButton);
}