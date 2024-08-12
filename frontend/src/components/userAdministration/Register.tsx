import {Alert, AlertTitle, Box, Button, Stack, TextField, Typography} from "@mui/material";
import {FormEvent, useState} from "react";
import {getUrlFrom} from "../../util/functions.ts";

export default function Register() {

    const [email, setEmail] = useState("")
    const [pass, setPass] = useState("")
    const [pass2, setPass2] = useState("")
    const [emailOk, setEmailOk] = useState(true)
    const [passOk, setPassOk] = useState(true)
    const [success, setSuccess] = useState(false)
    const [showPassword, setShowPassword] = useState(false)

    interface RegisterRequestData {
        email: string,
        password: string
    }

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {

        // console.log(`email: ${email}, pass: ${pass}, pass2: ${pass2}`)
        event.preventDefault()
        validateEmail(email)
        validatePassword(pass)

        if (!emailOk || !passOk || pass !== pass2) {
            setPass2("")
            return
        }
        const registerUrl = getUrlFrom("user", "register")
        fetch(registerUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                    email: email,
                    password: pass
                } as RegisterRequestData
            )
        }).then(response => {
            if (response.status === 201) {
                console.log("registered")
                setSuccess(true)
            } else {
                console.log("registration failed")
            }
        })
    }

    const validateEmail = (email: string) => {
        return setEmailOk(email.includes("@"));
    }

    const validatePassword = (password: string) => {
        return setPassOk(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/.test(password)
        )
    }

    return (
        <Box component="form" onSubmit={handleSubmit}>
            <Typography variant={"h3"}>Jetzt registrieren</Typography>
            <Typography gutterBottom={true}>Gib deine DNT-Email-Adresse ein und vergib ein sicheres
                Passwort</Typography>
            <TextField
                margin="normal"
                required
                fullWidth
                id="email"
                label="Email Adresse"
                name="email"
                autoComplete="email"
                autoFocus
                onChange={(e) => {
                    setEmail(e.target.value)
                }}
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
                }}
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
            />
            <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{mt: 3, mb: 2}}
            >Registrieren</Button>
            <Stack spacing={2}>
                {success ?
                    <Alert severity="success">Registrierung erfolgreich, bitte überprüfe deine Mails</Alert> : null}
                {emailOk ? null : <Alert severity="error"><AlertTitle>Email Adresse ungültig</AlertTitle>
                    Email Adresse gehört nicht zu einem gültigen Format.
                </Alert>}
                {pass === pass2 ? null : <Alert severity="error">Beide Passwörter sind nicht identisch</Alert>}
                {passOk ? null :
                    <Alert severity="error" hidden={pass !== pass2}><AlertTitle>Passwort unsicher</AlertTitle>
                        Das Passwort muss mindestens 8 Zeichen haben, aus Groß- und Kleinbuchstaben, Zahlen und
                        Sonderzeichen bestehen.
                    </Alert>}
            </Stack>
        </Box>
    )
}
