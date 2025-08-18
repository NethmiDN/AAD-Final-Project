document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    form.addEventListener('submit', async function (e) {
        e.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch('http://localhost:8080/auth/barkbuddy/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });
            const result = await response.json();
            if (response.ok && result.data && result.data.accessToken) {
                // Save token (e.g. localStorage) and redirect
                localStorage.setItem('accessToken', result.data.accessToken);
                alert('Login successful!');
                window.location.href = '/pages/dashboard.html';
            } else {
                alert('Login failed: ' + (result.status || 'Unknown error'));
            }
        } catch (err) {
            alert('Error connecting to server.');
        }
    });
});