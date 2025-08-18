const dropdownButton = document.getElementById('roleDropdown');

// Handle dropdown show/hide events for arrow rotation
dropdownButton.addEventListener('show.bs.dropdown', function () {
    this.classList.add('show');
});

dropdownButton.addEventListener('hide.bs.dropdown', function () {
    this.classList.remove('show');
});

// Handle dropdown selection
document.querySelectorAll('.dropdown-item').forEach(item => {
    item.addEventListener('click', function(e) {
        e.preventDefault();
        const selectedText = this.textContent;
        const selectedValue = this.getAttribute('data-value');

        // Update the button text
        document.getElementById('selectedRole').textContent = selectedText;

        // Update the hidden input value
        document.getElementById('role').value = selectedValue;

        // Close the dropdown (this will trigger the hide event and rotate arrow back)
        const dropdown = bootstrap.Dropdown.getInstance(dropdownButton);
        if (dropdown) {
            dropdown.hide();
        }
    });
});

// BarkBuddy_FrontEnd/js/index.js
document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    form.addEventListener('submit', async function (e) {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const role = document.getElementById('role').value;

        try {
            const response = await fetch('http://localhost:8080/auth/barkbuddy/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, email, password, role })
            });
            const result = await response.json();
            if (response.ok && result.data) {
                alert('Registration successful!');
                window.location.href = '/pages/login.html';
            } else {
                alert('Registration failed: ' + (result.status || 'Unknown error'));
            }
        } catch (err) {
            alert('Error connecting to server.');
        }
    });
});