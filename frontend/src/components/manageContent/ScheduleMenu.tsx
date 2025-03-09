import {Alert, Autocomplete, Box, Button, Fade, Stack, TextField, Typography} from "@mui/material";
import {FormEvent, useEffect, useState} from "react";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs"
import "dayjs/locale/de";
import {getUrlFrom} from "../../util/functions.ts";


export default function ScheduleMenu() {
    const [start, setStart] = useState(dayjs().add(6, 'day').day(1));
    const [end, setEnd] = useState(dayjs().add(6, 'day').day(5));
    const [newDishes, setNewDishes] = useState<string[]>(["", "", "", "", ""]);
    const [dishes, setDishes] = useState<string[]>([]);
    const [submitted, setSubmitted] = useState(false)
    const [error, setError] = useState(false)

    const fetchDishes = async () => {
        let dishesUrl = getUrlFrom("info", "dishes")
        const response = await fetch(dishesUrl, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            }
        });
        const data = await response.json();
        setDishes(data);
    };

    useEffect(() => {
        // Fetch dish names when the component mounts


        fetchDishes().then(() => console.log("Fetched dishes")).catch((reason) => console.log(`could not fetch dishes: ${reason}`));
    }, []); // Empty dependency array means this effect runs once on mount

    useEffect(() => {
        if (submitted) {
            const timer = setTimeout(() => {
                setSubmitted(false)
            }, 10000)
            return () => clearTimeout(timer)
        }
    }, [submitted])

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const formData = {
            validFrom: start.format("YYYY-MM-DD"),
            validTo: end.format("YYYY-MM-DD"),
            dishes: newDishes.map((dish, index) => ({id: 0, name: dish, day: index + 1}))
        };
        console.log(formData);

        let menuUrl = getUrlFrom("content", "new-menu")
        fetch(menuUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            },
            body: JSON.stringify(formData)
        })
            .then(resp => {
                if (resp.status !== 201) {
                    setError(true)
                } else {
                    setError(false)
                    setSubmitted(true)
                }
            })
    }

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="de">
            <Stack component={"form"} onSubmit={handleSubmit}
                   sx={{display: 'flex', flexDirection: 'column'}}>

                <Typography variant={"h2"}>Neues Menü planen</Typography>

                <Stack spacing={2} direction={"row"}>
                    <DatePicker label={"Start"}
                                value={start}
                                onChange={(newValue) => {
                                    setStart(newValue);
                                    setEnd(newValue.add(5 - start.day(), 'day'))

                                }}
                    />
                    <Box sx={{flexGrow: 1}}/>
                    <DatePicker label={"Ende"}
                                value={end}
                                onChange={(newValue) => setEnd(newValue)}
                    />
                </Stack>


                {["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"].map((day, idx) => (
                    <WeekdayScheduler key={day}
                                      day={day}
                                      handleChange={(newValue: string) => {
                                          const temp = [...newDishes]
                                          temp[idx] = newValue
                                          setNewDishes(temp)
                                      }}
                                      dishes={dishes}/>))}

                <Button variant={"contained"} type={"submit"} sx={{mt: 2, mb: 3}}>Speichern</Button>
                {submitted && <Fade in={submitted}>
                    <Alert severity="success">Menü gespeichert</Alert>
                </Fade>}
                {error && <Fade in={error}>
                    <Alert severity="error">Fehler beim Speichern</Alert>
                </Fade>}
            </Stack>
        </LocalizationProvider>
    );
}


function WeekdayScheduler({day, handleChange, dishes}: {
    day: string,
    handleChange: (value: string) => void
    dishes: string[]
}) {

    return (
        <Autocomplete onInputChange={(_e, newVal) => handleChange(newVal)}
                      options={dishes}
                      freeSolo
                      renderInput={(params) =>
                          <TextField {...params}
                                     variant="outlined"
                                     margin={"normal"}
                                     fullWidth
                                     label={day}
                                     type={"text"}
                          />}
        />
    )
}