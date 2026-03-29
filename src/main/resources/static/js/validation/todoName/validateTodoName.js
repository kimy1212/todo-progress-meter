export const TodoNameValidationResult = {
	OK: "OK",
	EMPTY: "EMPTY",
	DUPLICATE: "DUPLICATE",
	TOO_LONG: "TOO_LONG",
};

const TODO_NAME_MAX_LENGTH = 255;

/**
 * todo名検証
 *
 * @param {string} name todo名
 * @param {string[]} existingNames 既存のtodo名一覧
 */
export function validateTodoName(name, existingNames) {
	if (!name || name.trim() === "") {
		return TodoNameValidationResult.EMPTY;
	}
	if (name.trim().length > TODO_NAME_MAX_LENGTH) {
		return TodoNameValidationResult.TOO_LONG;
	}
	if (existingNames.includes(name.trim())) {
		return TodoNameValidationResult.DUPLICATE;
	}
	return TodoNameValidationResult.OK;
}
