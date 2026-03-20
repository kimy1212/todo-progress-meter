/**
 * 共通部品
 */

/**
 * エラー画面遷移
 * 
 * @param {number} status ステータスコード
 */
export function redirectByStatusCode(status) {
	switch (status) {
		case 401:
			window.location.href = '/error/401';
			break;
		case 404:
			window.location.href = '/error/404';
			break;
		case 500:
			window.location.href = '/error/500';
			break;
		default:
			redirectToGenericError();
			break;
	}
}

export function redirectToGenericError() {
	window.location.href = '/error/general';
}

/**
 * inputのインラインエラーを表示する
 *
 * @param {HTMLElement} input
 * @param {string} message
 */
export function showInputError(container, message, inputEl) {
	clearInputError(container);
	const error = document.createElement('span');
	error.className = 'input-error';
	error.textContent = message;
	container.classList.add('has-input-error');
	container.appendChild(error);
	inputEl?.addEventListener('input', () => clearInputError(container), { once: true });
}

/**
 * インラインエラーを削除する
 *
 * @param {HTMLElement} container
 */
export function clearInputError(container) {
	container.querySelector('.input-error')?.remove();
	container.classList.remove('has-input-error');
}

/**
 * テキスト入力項目作成
 */
export function createInputText(text, className) {
	const input = document.createElement('input');
	input.type = 'text';
	input.value = text;
	if (Array.isArray(className)) {
		input.classList.add(...className);
	} else if (typeof className === 'string') {
		input.classList.add(className);
	}
	return input;
}

/** ラベル作成 */
export function createLabel(tagName, text, className) {
	const el = document.createElement(tagName);
	el.textContent = text;
	if (Array.isArray(className)) {
		el.classList.add(...className);
	} else if (typeof className === 'string') {
		el.classList.add(className);
	}
	return el;
}

/**
 * input→label変換処理
 */
export function replaceInputWithLabel(input, tagName, className) {
	if (input._replaced) return;
	input._replaced = true;

	input.parentNode.replaceChild(createLabel(tagName, input.value, className), input);
}

/**
 * label→input変換処理
 */
export function replaceLabelWithInput(label, className) {
	const input = createInputText(label.textContent, className);
	input.dataset.originalValue = label.textContent;
	label.parentNode.replaceChild(input, label);
}

/**
 * 文字列を整数に変換する。空文字の場合はnullを返す。
 *
 * @param {string} value 変換対象の文字列
 * @returns {number|null}
 */
export function toIntOrNull(value) {
	return value !== '' ? parseInt(value, 10) : null;
}