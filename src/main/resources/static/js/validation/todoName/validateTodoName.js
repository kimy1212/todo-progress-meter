export const TodoNameValidationResult = {
	OK: "OK",
	EMPTY: "EMPTY",
	DUPLICATE: "DUPLICATE",
};


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
	if (existingNames.includes(name.trim())) {
		return TodoNameValidationResult.DUPLICATE;
	}
	return TodoNameValidationResult.OK;
}
