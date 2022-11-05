async function sendLoginData() {
    let userData = {
        username: document.getElementById("username-id").value,
        password: document.getElementById("password-id").value
    };

    let response = await fetch('/auth', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        body: JSON.stringify(userData),
    });

    let result = await response.text();

    switch (result) {
        case 'true': {
            alert(`User ${userData.username} is authenticated!`);
            location.reload();
            break;
        }
        case 'false': {
            alert(`User is NOT authenticated!`);
            break;
        }
    }
}

function signOut() {
    fetch('/logout', {method: 'GET'}).then(() => {
        alert('User has logged out');
        location.reload();
    });
}