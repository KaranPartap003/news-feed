import { useRef, useState, useEffect } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import AuthService from "./Services/AuthService.jsx";
import { isEmail } from "validator";


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

const validEmail = (val) => {
    if(!isEmail(val)){
        return (    
            <div className="alert alert-danger" role="alert">
                This is not a valid email.
            </div>
        );
    }
}

export default function SignUp() {
    const form = useRef();
    const [userName, setName] = useState('');
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const [password, setPassword] = useState('');
    const [successful, setSuccessful] = useState(false);

    const onChangeName = (e) =>{
        const name  = e.target.value;
        setName(name);
    }

    const onChangeEmail = (e) =>{
        const email = e.target.value;
        setEmail(email);
    }

    const onChangePassword = (e) => {
        const password = e.target.value;
        setPassword(password);
    }

    const handleResigter = (e) => {
        e.preventDefault();

        const isFormValid = userName.trim().length > 0 && isEmail(email) && password.length >= 6;

        if(isFormValid){
            AuthService.register(userName, email, password).then(
                (response) => {
                    setMessage(response.data.message)
                    console.log(response.data.message);//validation received from backend
                    setSuccessful(true);
                },
                (error) => {
                    setMessage(error.toString());
                    console.log(error.toString());
                    setSuccessful(false);
                }
            );
        }
        else{
            setMessage("Please Enter Valid Credentials");
            setSuccessful(false);
        }
    }
    return(
        <Form onSubmit={handleResigter} className="card bg-opacity-75 border-0 mx-auto  p-4 mt-5 form-class"
            ref={form}>
            <div className="mb-3">
            <h1>Sign up</h1>
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
            <label className="label">Email address</label>
            <Input
                type="email"
                className="form-control"
                placeholder="Enter email"
                value={email}
                validations={[required, validEmail]}
                onChange={onChangeEmail}
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
            <br/>
            {message && (
                <div className={successful ? 'succeeded' : 'failed'} role='alert'>
                    {message}
                </div>
            )}
        </Form>
    )
}