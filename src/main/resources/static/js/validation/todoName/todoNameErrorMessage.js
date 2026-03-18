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
		case TodoNameValidationResult.DUPLICATE:
			return "同じ名前のTODOがすでに存在します";
		default:
			return;
	}
}
