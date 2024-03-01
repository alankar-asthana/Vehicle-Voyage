function login(email, password) {
  const requestBody = {
    email: email,
    password: password
  };

  fetch('http://localhost:8080/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams(requestBody) // create a URLSearchParams object
  })
  .then(response => response.json())
  .then(data => handleLoginResponse(data))
  .catch(error => console.error('Error:', error));
}

function handleLoginResponse(response) {
  // Store the token in local storage
  setTokenToLocalStorage(response.jwtToken);
  let role = response.role;
  console.log(role);

  // Redirect to the desired dashboard URL
  if (role.includes("USER")) {
    redirectToUserDashboard(response.jwtToken);
  } else {
    redirectToAdminDashboard(response.jwtToken);
  }
}

function redirectToUserDashboard(token) {
  const headers = addTokenToHeaders(token);
  fetch('/user/userdashboard', {
    method: 'GET',
    headers: headers
  })
  .then(response => {
    if (!response.ok) {
      if (response.status === 401) {
        console.error('Unauthorized: Please log in');
        window.location.href = '/login';
      } else {
        console.error('Error:', response.status);
      }
    } else {
      return response.text();
    }
  })
  .then(data => {
    const userDashboard = document.createElement('div');
    userDashboard.innerHTML = data;
    document.body.appendChild(userDashboard);
  })
  .catch(error => console.error('Error:', error));
}

function redirectToAdminDashboard(token) {
  const headers = addTokenToHeaders(token);
  fetch('/admin/admindashboard', {
    method: 'GET',
    headers: headers
  })
  .then(response => {
    if (!response.ok) {
      if (response.status === 401) {
        console.error('Unauthorized: Please log in');
        window.location.href = '/login';
      } else {
        console.error('Error:', response.status);
      }
    } else {
      return response.text();
    }
  })
  .then(data => {
    const adminDashboard = document.createElement('div');
    adminDashboard.innerHTML = data;
    document.body.appendChild(adminDashboard);
  })
  .catch(error => console.error('Error:', error));
}

function setTokenToLocalStorage(token) {
  localStorage.setItem('vehicleVoyageToken', token);
}

function getTokenFromLocalStorage() {
  return localStorage.getItem('vehicleVoyageToken');
}

function addTokenToHeaders(token) {
  const headers = new Headers();
  headers.append('Authorization', 'Bearer ' + token);
  return headers;
}

function fetchDataWithToken(url) {
  const token = getTokenFromLocalStorage();
  if (!token) {
    console.error('No token found');
    window.location.href = '/auth/login';
    return;
  }

  const headers = addTokenToHeaders(token);
  fetch(url, {
    method: 'GET',
    headers: headers
  })
  .then(response => {
    if (!response.ok) {
      if (response.status === 401) {
        console.error('Unauthorized: Please log in');
        window.location.href = '/login';
      } else {
        console.error('Error:', response.status);
      }
    } else {
      return response.json();
    }
  })
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));
}









  // Determine the redirect URL based on the role
 // Determine the redirect URL based on the role
//   if (role.includes("USER")) {
//     let redirectUrl = '/user/userdashboard';
//     window.location.href = redirectUrl;
//   } else if (role.includes("ADMIN")) {
//     let redirectUrl = '/admin/admindashboard';
//     window.location.href = redirectUrl;
//   } else {
//     console.error("Invalid role: ", role);
//   }