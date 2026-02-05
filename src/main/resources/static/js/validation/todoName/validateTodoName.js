export const TodoNameValidationResult = {
	OK: "OK",
	EMPTY: "EMPTY",
	TOO_LONG: "TOO_LONG",
};


/**
 * todo名検証
 * 
 * @param {string} name todoタブ名
 */
export function validateTodoName(name) {
	if (!name || name.trim() === "") {
		return TodoNameValidationResult.EMPTY;
	}
	if (name.length > 50) {
		return TodoNameValidationResult.TOO_LONG;
	}
	return TodoNameValidationResult.OK;
}
