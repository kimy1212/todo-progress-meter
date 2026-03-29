export const TodoTabNameValidationResult = {
	OK: "OK",
	EMPTY: "EMPTY",
	DUPLICATE: "DUPLICATE",
	TOO_LONG: "TOO_LONG",
};

const TODO_TAB_NAME_MAX_LENGTH = 255;

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
	if (name.trim().length > TODO_TAB_NAME_MAX_LENGTH) {
		return TodoTabNameValidationResult.TOO_LONG;
	}
	if (existingNames.includes(name.trim())) {
		return TodoTabNameValidationResult.DUPLICATE;
	}
	return TodoTabNameValidationResult.OK;
}