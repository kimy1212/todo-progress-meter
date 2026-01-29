export const TodoTabNameValidationResult = {
	OK: "OK",
	EMPTY: "EMPTY",
	TOO_LONG: "TOO_LONG",
};


/**
 * todoタブ名検証
 * 
 * @param {string} name todoタブ名
 */
export function validateTodoTabName(name) {
	if (!name || name.trim() === "") {
		return TodoTabNameValidationResult.EMPTY;
	}
	if (name.length > 30) {
		return TodoTabNameValidationResult.TOO_LONG;
	}
	return TodoTabNameValidationResult.OK;
}