import { useRef, useState, useEffect } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import AuthService from "./Services/AuthService.jsx";


const required = (value) => {
    if (!value) {
        return (
            <div className="alert alert-danger" backgroundcolor="#1a1a1a" role="alert">
                This field is required!
            </div>
        );
    }
};

const validName = (val) => {
    if(val.trim().length < 3 || val.trim().length > 20){
        return (    
            <div className="alert alert-danger" role="alert">
                Name must be between 3 and 20 characters.
            </div>
        );
    }
}

const validPassword = (val) => {
    if(val.length < 6 || val.length > 40){
        return (
            <div className="alert alert-danger" role="alert">
                Password must be between 6 and 40 characters.
            </div>
        )
    }
}

export default function Login() {
    const [userName, setName] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [successful, setSuccessful] = useState(false);

    const onChangeName = (e) =>{
        const name  = e.target.value;
        setName(name);
    }

    const onChangePassword = (e) => {
        const password = e.target.value;
        setPassword(password);
    }

    const handleResigter = (e) => {
        e.preventDefault();

        const isFormValid = userName.trim().length > 0 && password.length >= 6;

        if(isFormValid){
            AuthService.login(userName, password).then(
                (response) => {
                    setMessage(response.data.message);//validation received from backend
                    console.log(response.data.message);
                    setSuccessful(true);
                },
                (error) => {
                    message(error.response.data.message);
                    console.log(error.toString());
                    setSuccessful(false);
                }
            );
        }
        else{
            setSuccessful(false);
            setMessage("Please Enter Valid Credentials");
        }
    }
    return(
        <Form onSubmit={handleResigter} className="card bg-opacity-75 border-0 mx-auto  p-4 mt-5 form-class" >
            <div className="mb-3">
            <h1>Login</h1>
            <br />
            <label className="label">User name</label>
            <Input
                type="text"
                className="form-control"
                placeholder="Enter user name"
                onChange={onChangeName}
                validations={[required, validName]}
                value={userName}
            />
            </div>
            <div className="mb-3">
            <label className="label">Password</label>
            <Input
                type="password"
                className="form-control"
                placeholder="Enter password"
                value={password}
                validations={[required, validPassword]}
                onChange={onChangePassword}
            />
            </div>
            <div className="d-grid">
            <button type="submit" className="button" >
                Submit
            </button>
            </div>
            <br />
            {message && (
              <div className={successful? "succeded" : "failed"} role="alert">
                {message}
              </div>
            )}
        </Form>
    )
}