export const TodoTabNameValidationResult = {
	OK: "OK",
	EMPTY: "EMPTY",
	DUPLICATE: "DUPLICATE",
};


/**
 * todoタブ名検証
 *
 * @param {string} name todoタブ名
 * @param {string[]} existingNames 既存のtodoタブ名一覧
 */
export function validateTodoTabName(name, existingNames) {
	if (!name || name.trim() === "") {
		return TodoTabNameValidationResult.EMPTY;
	}
	if (existingNames.includes(name.trim())) {
		return TodoTabNameValidationResult.DUPLICATE;
	}
	return TodoTabNameValidationResult.OK;
}