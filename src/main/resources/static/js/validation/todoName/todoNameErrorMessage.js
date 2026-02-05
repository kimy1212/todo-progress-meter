import { TodoNameValidationResult } from './validateTodoName.js';

/**
 * todo名エラーメッセージ
 * 
 * @param {string} result エラーチェック結果
 */
export function todoNameErrorMessage(result) {
	switch (result) {
		case TodoNameValidationResult.EMPTY:
			return "TODOを入力してください";
		case TodoNameValidationResult.TOO_LONG:
			return "TODOは50文字以内で入力してください";
		default:
			return;
	}
}
