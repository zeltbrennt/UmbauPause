import {
    Avatar,
    Box,
    Button,
    Checkbox,
    Dialog,
    DialogActions,
    DialogContent,
    FormControlLabel,
    Grid,
    Link,
    TextField,
    Typography
} from "@mui/material";
import LockPersonOutlinedIcon from '@mui/icons-material/LockPersonOutlined';
import {useState} from "react";

interface LoginDialogProps {
    open: boolean,
    handleClose: () => void,
    setCurrentUser: (user: string) => void
}

interface LoginRequestData {
    email: string,
    password: string
}

export default function LoginDialog({open, handleClose, setCurrentUser}: LoginDialogProps) {

    const api_url = "http://localhost:8080/login"
    const [loginError, setLoginError] = useState(false)

    const getToken = async (loginData: LoginRequestData) => {
        const response = await fetch(api_url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: loginData.email,
                password: loginData.password
            })
        })
        return await response.json()
    }
    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        getToken({
            email: data.get('email') as string,
            password: data.get('password') as string
        }).then(token => {
            console.log(token)
            //  const {decodedToken} = useJwt(token)
//            setCurrentUser(decodedToken.username)
            setCurrentUser(data.get('email') as string)
            handleClose()
        }).catch(reason => {
            console.log(`could not login: ${reason}`)
            setLoginError(true)

        })
    };
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <DialogContent>
                <Box
                    sx={{
                        marginTop: 8,
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
                            label="Email Address"
                            name="email"
                            autoComplete="email"
                            autoFocus
                            error={loginError}
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
                            error={loginError}
                        />
                        <FormControlLabel
                            control={<Checkbox value="remember" color="primary"/>}
                            label="Remember me"
                        />
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
                                <Link href="#" variant="body2">
                                    {"Noch keinen Account? Hier anmelden."}
                                </Link>
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