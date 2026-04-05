/**
 * 進捗率編集画面
 */
import * as common from './common/common.js';

let tabId;
let todoId;

//イベント付与
document.getElementById('progressRateModalOverlay').addEventListener('click', (event) => {
	if (event.target === event.currentTarget) closeProgressRateModal();
});
document.getElementById('progressRateModalCloseButton').addEventListener('click', closeProgressRateModal);
document.getElementById('progressRateModalCancelButton').addEventListener('click', closeProgressRateModal);

document.getElementById('progressRateCompleted').addEventListener('input', truncateInputValue);
document.getElementById('progressRateCompleted').addEventListener('input', updateModalPreview);
document.getElementById('progressRateCompleted').addEventListener('input', updateSaveButtonState);
document.getElementById('progressRateTotal').addEventListener('input', truncateInputValue);
document.getElementById('progressRateTotal').addEventListener('input', updateModalPreview);
document.getElementById('progressRateTotal').addEventListener('input', updateSaveButtonState);

document.getElementById('progressRateModalSaveButton').addEventListener('click', handleProgressRateSaveButtonClick);

/**
 * 初期表示
 *
 * @param {number} activeTabId アクティブtodoタブID
 * @param {number} activeTodoId 編集対象のtodoID
 * @param {number} completed 完了数（分子）
 * @param {number} total 全体数（分母）
 */
export function init(activeTabId, activeTodoId, completed, total) {
	tabId = activeTabId;
	todoId = activeTodoId;
	openProgressRateModal(completed, total);
}

/**
 * 進捗率モーダルを開く
 *
 * @param {number} completed 完了数（分子）
 * @param {number} total 全体数（分母）
 */
function openProgressRateModal(completed, total) {
	document.querySelectorAll('.todo-actions-menu.is-open').forEach(e => e.classList.remove('is-open'));
	document.getElementById('progressRateCompleted').value = completed || '';
	document.getElementById('progressRateTotal').value = total || '';
	updateModalPreview();
	updateSaveButtonState();
	common.clearInputError(document.getElementById('progressRateInputs'));
	document.getElementById('progressRateModalOverlay').classList.add('is-open');
	document.getElementById('progressRateCompleted').focus();
	document.getElementById('todoListScreen').inert = true;
}

/**
 * 進捗率モーダルを閉じる
 */
function closeProgressRateModal() {
	document.getElementById('progressRateModalOverlay').classList.remove('is-open');
	document.getElementById('todoListScreen').inert = false;
}

/**
 * input の値を整数に切り捨てて表示する
 *
 * @param {Event} event
 */
function truncateInputValue(event) {
	const input = event.target;
	if (input.value.includes('.')) {
		input.value = parseInt(input.value, 10);
	}
}

/**
 * 保存ボタンの活性・非活性を更新する
 */
function updateSaveButtonState() {
	const completed = document.getElementById('progressRateCompleted').value;
	const total = document.getElementById('progressRateTotal').value;
	document.getElementById('progressRateModalSaveButton').disabled = completed === '' || total === '';
}

/**
 * モーダルのドーナツチャートプレビューを更新する
 */
function updateModalPreview() {
	const completed = Number(document.getElementById('progressRateCompleted').value) || 0;
	const total = Number(document.getElementById('progressRateTotal').value) || 0;
	const rate = total > 0 ? Math.round((completed / total) * 100) : 0;
	const clamped = Math.min(100, Math.max(0, rate));

	document.getElementById('modalProgressRateValue').textContent = clamped;
	document.getElementById('modalDonutChart').style.setProperty('--value', clamped);
}

/**
 * 進捗率更新
 */
async function updateProgressRate() {
	try {
		const dto = {
			completed: common.toIntOrNull(document.getElementById('progressRateCompleted').value),
			total: common.toIntOrNull(document.getElementById('progressRateTotal').value)
		}

		const res = await fetch(`/api/tabs/${tabId}/todos/${todoId}`, {
			method: 'PATCH',
			headers: { 'Content-Type': 'application/json', 'X-Requested-With': 'XMLHttpRequest' },
			body: JSON.stringify(dto),
		});

		if (!res.ok) {
			common.redirectByStatusCode(res.status);
			return false;
		}

		return true;
	} catch {
		common.redirectToGenericError();
		return false;
	}
}

async function handleProgressRateSaveButtonClick() {
	const progressRateInputs = document.getElementById('progressRateInputs');
	common.clearInputError(progressRateInputs);

	const completed = parseInt(document.getElementById('progressRateCompleted').value, 10);
	const total = parseInt(document.getElementById('progressRateTotal').value, 10);
	if (completed > total) {
		common.showInputError(progressRateInputs, '完了数は全体数以下にしてください');
		return;
	}

	const saveButton = document.getElementById('progressRateModalSaveButton');
	saveButton.disabled = true;
	try {
		const isUpdated = await updateProgressRate();
		if (!isUpdated) return;
		closeProgressRateModal();
		sessionStorage.setItem('activeTabId', tabId);
		window.location.href = '/';
	} finally {
		updateSaveButtonState();
	}
}