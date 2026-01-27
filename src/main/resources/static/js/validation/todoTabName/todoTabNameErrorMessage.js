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
		case TodoTabNameValidationResult.TOO_LONG:
			return "タブは30文字以内で入力してください";
		default:
			return;
	}
}