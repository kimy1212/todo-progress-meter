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
