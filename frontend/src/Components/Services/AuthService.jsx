import axios from 'axios';

const API_URL = 'http://localhost:8082/auth/';

const register = async (username, email, password) => {
    return axios.post(API_URL + "register", {
    username,
    email,
    password,
  });
}

const login = async (username, password) => {
    return axios
    .post(API_URL + "login", {
      username,
      password,
    })
    .then((response) => {
      if (response.data.accessToken) {
        localStorage.setItem("user", JSON.stringify(response.data));
      }

      return response.data;
    });
}

const logout = () => {
    localStorage.removeItem("user");
}

const authHeader = () => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (user && user.accessToken) {
        console.log("user's token: ", user.accessToken);
        return { Authorization: 'Bearer ' + user.accessToken };
    } else {
        console.log("No user found in local storage or no access token available.");
        return {};
    }
}

const AuthService = {
    register,
    login,
    logout,
    authHeader
};

export default AuthService;
