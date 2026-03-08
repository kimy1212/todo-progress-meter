/**
 * 進捗率編集画面
 */

/**
 * 進捗率モーダルを開く
 */
/**
 * @param {number} completed 完了数（分子）
 * @param {number} total 全体数（分母）
 */
export function openProgressRateModal(completed, total) {
	document.querySelectorAll('.todo-actions-menu.is-open').forEach(e => e.classList.remove('is-open'));
	document.getElementById('progressRateCompleted').value = completed || '';
	document.getElementById('progressRateTotal').value = total || '';
	updateModalPreview();
	document.getElementById('progressRateModalOverlay').classList.add('is-open');
	document.getElementById('progressRateCompleted').focus();
}

/**
 * 進捗率モーダルを閉じる
 */
export function closeProgressRateModal() {
	document.getElementById('progressRateModalOverlay').classList.remove('is-open');
}

/**
 * モーダルのドーナツチャートプレビューを更新する
 */
export function updateModalPreview() {
	const completed = Number(document.getElementById('progressRateCompleted').value) || 0;
	const total = Number(document.getElementById('progressRateTotal').value) || 0;
	const rate = total > 0 ? Math.round((completed / total) * 100) : 0;
	const clamped = Math.min(100, Math.max(0, rate));

	document.getElementById('modalProgressRateValue').textContent = clamped;
	document.getElementById('modalDonutChart').style.setProperty('--value', clamped);
}