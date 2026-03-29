/**
 * CSRF: POST/PATCH/DELETE に X-CSRF-TOKEN を自動付与
 */
const _originalFetch = window.fetch;
window.fetch = function(url, options = {}) {
	const method = (options.method || 'GET').toUpperCase();
	if (method !== 'GET') {
		const token = document.querySelector('meta[name="_csrf"]')?.content;
		const header = document.querySelector('meta[name="_csrf_header"]')?.content;
		if (token && header) {
			options.headers = { ...options.headers, [header]: token };
		}
	}
	return _originalFetch(url, options);
};

const trigger = document.getElementById('userMenuTrigger');
const dropdown = document.getElementById('userMenuDropdown');

if (trigger && dropdown) {
	trigger.addEventListener('click', (e) => {
		e.stopPropagation();
		const isOpen = dropdown.classList.toggle('is-open');
		trigger.setAttribute('aria-expanded', isOpen);
		dropdown.setAttribute('aria-hidden', !isOpen);
	});

	document.addEventListener('click', () => {
		dropdown.classList.remove('is-open');
		trigger.setAttribute('aria-expanded', false);
		dropdown.setAttribute('aria-hidden', true);
	});

	dropdown.addEventListener('click', (e) => e.stopPropagation());

}
