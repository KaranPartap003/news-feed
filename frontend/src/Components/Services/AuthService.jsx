import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

const register = async (username, email, password) => {
    try{
        return await axios.post(API_URL + 'signup', {
            username,
            email,
            password,
        });
    }catch (error) {
        console.error("Error during registration:", error);
        throw error; // Rethrow the error to handle it in the calling function
    }
}

const login = async (username, password) => {
    try{
        return await axios.post(API_URL + 'login', {
            username,
            password,
        }).then((response) =>{
            if(response.data.accessToken){
                localStorage.setItem("user", JSON.stringify(response.data)); //store JWT token in local storage
                console.log("User data stored in local storage:", response.data);
            }
            return response.data;
        })
    }catch (error) {
        console.error("Error during login:", error);
        throw error; // Rethrow the error to handle it in the calling function
    }
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
