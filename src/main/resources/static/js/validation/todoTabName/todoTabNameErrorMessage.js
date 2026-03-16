import { TodoTabNameValidationResult } from './validateTodoTabName.js';

/**
 * todoタブ名エラーメッセージ
 * 
 * @param {string} result エラーチェック結果
 */
export function todoTabNameErrorMessage(result) {
	switch (result) {
		case TodoTabNameValidationResult.EMPTY:
			return "タブを入力してください";
		case TodoTabNameValidationResult.DUPLICATE:
			return "同じ名前のタブがすでに存在します";
		default:
			return;
	}
}