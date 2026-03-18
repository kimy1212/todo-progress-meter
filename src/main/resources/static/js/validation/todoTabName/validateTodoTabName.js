export const TodoTabNameValidationResult = {
	OK: "OK",
	EMPTY: "EMPTY",
	DUPLICATE: "DUPLICATE",
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
	return TodoTabNameValidationResult.OK;
}