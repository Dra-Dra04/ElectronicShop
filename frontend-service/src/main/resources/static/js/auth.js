const CUSTOMER_API = "http://localhost:8082";

function getAuthToken() {
    return localStorage.getItem("authToken");
}

function getAuthHeader() {
    const token = getAuthToken();
    if (!token) return {};
    return {
        "Authorization": "Basic " + token
    };
}

function getCurrentUser() {
    const json = localStorage.getItem("currentUser");
    return json ? JSON.parse(json) : null;
}

function isLoggedIn() {
    return !!getAuthToken();
}

function logout() {
    localStorage.removeItem("authToken");
    localStorage.removeItem("currentUser");
    window.location.href = "login.html";
}
