import {Box, Button, TextField} from "@mui/material";
import {FormEvent, useState} from "react";
import {useNavigate} from "react-router-dom";

export default function Register() {

    const navigate = useNavigate()
    const [email, setEmail] = useState("")
    const [pass, setPass] = useState("")
    const [pass2, setPass2] = useState("")
    const [emailOk, setEmailOk] = useState(true)
    const [passOk, setPassOk] = useState(true)

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {

        event.preventDefault()
        if (!emailOk || !passOk || pass !== pass2) return
        fetch("http://localhost:8080/register", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                    email: email,
                    password: pass
                }
            )
        }).then(response => {
            if (response.status === 201) {
                console.log("registered")
                navigate("/")
            } else {
                console.log("registration failed")
            }
        })
    }

    const validateEmail = (email: string) => {
        return setEmailOk(email.includes("@"));
    }

    const validatePassword = (password: string) => {
        return setPassOk(password.length > 8)
    }

    return (
        <Box component="form" onSubmit={handleSubmit}>
            <TextField
                margin="normal"
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                autoFocus
                onChange={(e) => {
                    setEmail(e.target.value)
                    if (!emailOk) validateEmail(e.target.value)
                }}
                onBlur={() => validateEmail(email)}
                error={!emailOk && email.length > 0}
            />
            <TextField
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="current-password"
                onChange={(e) => {
                    setPass(e.target.value)
                    if (!passOk) validatePassword(e.target.value)
                }}
                onBlur={() => {
                    validatePassword(pass)
                }}
                error={!passOk && pass.length > 0}
            />
            <TextField
                margin="normal"
                required
                fullWidth
                name="password2"
                label="Password wiederholen"
                type="password"
                id="password2"
                autoComplete="current-password"
                onChange={(e) => setPass2(e.target.value)}
                error={pass !== pass2}
            />
            <Button
                type="submit"
                fullWidth
                variant="contained"
                //deactivated={email.length === 0 || pass.length === 0 || pass2.length === 0 || pass !== pass2 || !emailOk || !passOk}
                sx={{mt: 3, mb: 2}}
            >
                Register</Button>
        </Box>
    )
}