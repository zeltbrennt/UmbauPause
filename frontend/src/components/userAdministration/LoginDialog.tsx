import {
    Avatar,
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    FormControl,
    Grid,
    IconButton,
    InputAdornment,
    InputLabel,
    Link,
    OutlinedInput,
    TextField,
    Typography
} from "@mui/material";
import LockPersonOutlinedIcon from '@mui/icons-material/LockPersonOutlined';
import {FormEvent, useState} from "react";
import {jwtDecode} from "jwt-decode";
import {JWTToken, UserPrincipal} from "../../util/Interfaces.ts";
import {getUrlFrom} from "../../util/functions.ts";
import {useNavigate} from "react-router-dom";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";

interface LoginDialogProps {
    open: boolean,
    handleClose: () => void,
    setCurrentUser: (user: UserPrincipal) => void,
    handleRegister: () => void
}

interface LoginRequestData {
    email: string,
    password: string
}

export default function LoginDialog({open, handleClose, setCurrentUser, handleRegister}: LoginDialogProps) {

    const loginUrl = getUrlFrom("user", "login")
    const [loginError, setLoginError] = useState(false)
    const [showPassword, setShowPassword] = useState(false)

    const getToken = async (loginData: LoginRequestData) => {
        const response = await fetch(loginUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        })
        return await response.json()
    }
    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        getToken({
            email: data.get('email'),
            password: data.get('password')
        } as LoginRequestData)
            .then(response => {
                const decodedToken = jwtDecode<JWTToken>(response.accessToken)
                sessionStorage.setItem('accessToken', response.accessToken)
                sessionStorage.setItem('refreshToken', response.refreshToken)
                const userPrincipal = {
                    uid: decodedToken.uid,
                    roles: decodedToken.roles
                } as UserPrincipal
                sessionStorage.setItem('userPrincipal', JSON.stringify(userPrincipal))
                setCurrentUser(userPrincipal)
                setLoginError(false)
                handleClose()
                console.log(sessionStorage)
            }).catch(reason => {
            console.log(`could not login: ${reason}`)
            setLoginError(true)

        })
    };

    const navigate = useNavigate()

    return (
        <Dialog
            open={open}
            onClose={() => {
                handleClose();
                setLoginError(false)
            }}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <DialogContent>
                <Box
                    sx={{
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Avatar sx={{m: 1, bgcolor: 'secondary.main'}}>
                        <LockPersonOutlinedIcon/>
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        Sign in
                    </Typography>
                    <Box component="form" onSubmit={handleSubmit} noValidate sx={{mt: 1}}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            label="Email Adresse"
                            name="email"
                            autoComplete="email"
                            autoFocus
                            error={loginError}
                        />
                        <FormControl fullWidth variant="outlined" margin={"normal"} required>
                            <InputLabel htmlFor="outlined-adornment-password">Password</InputLabel>
                            <OutlinedInput
                                id="password"
                                name="password"
                                type={showPassword ? 'text' : 'password'}
                                error={loginError}
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
                        >
                            Anmelden
                        </Button>
                        <Grid container>
                            <Grid item xs>
                                <Link href="#" variant="body2">
                                    Passwort vergessen?
                                </Link>
                            </Grid>
                            <Grid item>
                                <Button variant="text" onClick={() => {
                                    handleRegister();
                                    handleClose()
                                    navigate("/register")
                                }}>
                                    Registrieren
                                </Button>
                            </Grid>
                        </Grid>
                    </Box>
                </Box>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Schließen</Button>
            </DialogActions>
        </Dialog>
    )
}