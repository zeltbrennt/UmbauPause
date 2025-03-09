import {
    Alert,
    AlertTitle,
    Box,
    Button,
    FormControl,
    IconButton,
    InputAdornment,
    InputLabel,
    OutlinedInput,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import {FormEvent, useState} from "react";
import {getUrlFrom} from "../../util/functions.ts";
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';

export default function Register() {

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
        const data = new FormData(event.currentTarget)
        const email = data.get('email') as string
        const pass = data.get('password') as string
        validateEmail(email)
        validatePassword(pass)
        if (!emailOk || !passOk) {
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
                error={!emailOk}
            />
            <FormControl
                fullWidth
                variant="outlined"
                margin={"normal"}
                required
                error={!passOk}>
                <InputLabel htmlFor="outlined-adornment-password">Password</InputLabel>
                <OutlinedInput
                    id="password"
                    name="password"
                    type={showPassword ? 'text' : 'password'}
                    endAdornment={
                        <InputAdornment position="end">
                            <IconButton
                                aria-label="toggle password visibility"
                                onClick={() => setShowPassword(!showPassword)}
                                edge="end"
                            >
                                {showPassword ? <Visibility/> : <VisibilityOff/>}
                            </IconButton>
                        </InputAdornment>
                    }
                    label="Passwort"
                />
            </FormControl>
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
                {passOk ? null :
                    <Alert severity="error"><AlertTitle>Passwort unsicher</AlertTitle>
                        Das Passwort muss mindestens 8 Zeichen haben, aus Groß- und Kleinbuchstaben, Zahlen und
                        Sonderzeichen bestehen.
                    </Alert>}
            </Stack>
        </Box>
    )
}
