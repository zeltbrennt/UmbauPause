import {Alert, Box, Button, Container, Fade, Stack, TextField, Typography} from "@mui/material";
import {FormEvent, useEffect, useState} from "react";
import VolunteerActivismIcon from '@mui/icons-material/VolunteerActivism';

export default function Feedback() {

    const [submitted, setSubmitted] = useState(false)
    const [name, setName] = useState("Anonym")
    const [feedback, setFeedback] = useState("")
    const [error, setError] = useState(false)
    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        const data = new FormData(event.currentTarget)
        const feedback = data.get("feedback")
        setSubmitted(true)
        //todo: send feedback to server
        console.log(`${name} submitted: ${feedback}`)
    }

    useEffect(() => {
        if (submitted) {
            const timer = setTimeout(() => {
                setSubmitted(false)
            }, 3000)
            return () => clearTimeout(timer)
        }
    }, [submitted])

    return (
        <Container>
            <Typography variant="h3">Feedback</Typography>
            <Typography marginTop={2} marginBottom={2}>
                Hallo, ich bin Konrad, der Entwickler dieser Webseite. Falls du Anregungen oder Kritik hast, dir
                bestimmte Funktionen fehlen
                oder du bei der Bedienung Schwierigkeiten hast, lass gerne ein Feedback da. Ich freue mich über jeden
                Hinweis, wie ich die Webseite besser machen kann.
            </Typography>
            <Typography marginBottom={4}>
                Falls sie dir besonders gut gefällt, freue ich mich natürlich auch über ein Lob, oder eine kleine
                Spende.
            </Typography>
            <Box component={"form"} onSubmit={handleSubmit}>
                <Stack spacing={2}>

                    <TextField label={"Name"} defaultValue={name} name="name"></TextField>
                    <TextField multiline
                               rows={10}
                               label={"Feedback"}
                               placeholder={"I like the app!"}
                               name="feedback"
                               onChange={(event) => setFeedback(event.target.value)}
                    />
                    <Stack direction={"row"} spacing={2}>

                        <Button fullWidth type={"submit"} variant={"contained"}
                                disabled={!feedback.trim()}>Abschicken</Button>
                        <Button fullWidth startIcon={<VolunteerActivismIcon/>} variant="outlined" onClick={() => {
                            console.log("buy me a coffee")
                            window.open("https://paypal.me/zeltbrennt/5")
                        }}>Buy me a coffee</Button>
                    </Stack>
                    <Fade in={submitted}>
                        <Alert severity="success">Vielen Dank für dein Feedback!</Alert>
                    </Fade>
                    <Fade in={error}>
                        <Alert severity="error">Es ist ein Fehler aufgetreten</Alert>
                    </Fade>
                </Stack>
            </Box>
        </Container>
    )
}