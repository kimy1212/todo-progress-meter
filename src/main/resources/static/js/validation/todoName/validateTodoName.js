export const TodoNameValidationResult = {
	OK: "OK",
	EMPTY: "EMPTY",
	DUPLICATE: "DUPLICATE",
};


/**
 * todo名検証
 *
 * @param {string} name todo名
 */
export function validateTodoName(name) {
	if (!name || name.trim() === "") {
		return TodoNameValidationResult.EMPTY;
	}
	return TodoNameValidationResult.OK;
}
