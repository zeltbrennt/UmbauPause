import {Alert, Box, Button, Container, Fade, Stack, TextField, Tooltip, Typography} from "@mui/material";
import {FormEvent, useEffect, useState} from "react";
import VolunteerActivismIcon from '@mui/icons-material/VolunteerActivism';
import {getUrlFrom} from "../util/functions.ts";

export default function Feedback() {

    const [submitted, setSubmitted] = useState(false)
    const [name, setName] = useState("Anonym")
    const [feedback, setFeedback] = useState("")
    const [error, setError] = useState(false)
    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        const data = new FormData(event.currentTarget)
        const name = data.get("name")?.toString() || "Anonym"
        setName(name)
        setFeedback(data.get("feedback") as string)

        fetch(getUrlFrom("feedback", name), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/text'
            },
            body: feedback
        })
            .then(response => {
                if (response.status !== 200) {
                    setError(true)
                } else {
                    setError(false)
                    setSubmitted(true)
                }
            })
        console.log(`${name} submitted: ${feedback}`)
    }

    useEffect(() => {
        if (submitted) {
            const timer = setTimeout(() => {
                setSubmitted(false)
            }, 10000)
            return () => clearTimeout(timer)
        }
    }, [submitted])

    return (
        <Container disableGutters maxWidth="md">
            <Typography variant="h3">Feedback</Typography>
            <Typography marginTop={2} marginBottom={2}>
                Hi 👋 Ich bin Konrad, der Entwickler dieser Webseite. Hast du Anregungen oder Kritik,
                fehlen dir bestimmte Funktionen, oder du hast bei der Bedienung Schwierigkeiten?
                Hast du vielleicht sogar einen Fehler gefunden?
                Dann lass gerne ein Feedback da. Ich freue mich über jeden
                Hinweis, wie ich die Webseite besser machen kann.
            </Typography>
            <Typography marginBottom={4}>
                Falls es dir hier besonders gut gefällt, freue ich mich natürlich auch über ein Lob, oder eine kleine
                Spende 🥰
            </Typography>
            <Box component={"form"} onSubmit={handleSubmit}>
                <Stack spacing={2}>
                    <Tooltip placement={"top-start"} arrow title={"Frei lassen für anonymes Feedback"}>
                        <TextField label={"Name"} placeholder={"Anonym"} name="name" error={!name.trim()}></TextField>
                    </Tooltip>
                    <TextField multiline
                               minRows={4}
                               maxRows={10}
                               label={"Feedback"}
                               placeholder={"Ich habe folgende Verbesserungsvorschläge..."}
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
                    {submitted &&
                        <Fade in={submitted}>
                            <Alert severity="success">Vielen Dank für dein Feedback!</Alert>
                        </Fade>}
                    {error && <Fade in={error}>
                        <Alert severity="error">Es ist ein Fehler aufgetreten</Alert>
                    </Fade>}
                </Stack>
            </Box>
        </Container>
    )
}