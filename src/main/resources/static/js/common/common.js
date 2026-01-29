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
	label.parentNode.replaceChild(createInputText(label.textContent, className), label);
}