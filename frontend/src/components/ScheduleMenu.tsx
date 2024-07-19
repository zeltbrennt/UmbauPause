import {Autocomplete, Box, Button, Grid, TextField, Typography} from "@mui/material";
import {FormEvent, useEffect, useState} from "react";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs"
import "dayjs/locale/de";


export default function ScheduleMenu() {
    const weekdays = ["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"]
    const [start, setStart] = useState(dayjs().add(1, 'week').day(1));
    const [end, setEnd] = useState(dayjs().add(1, 'week').day(5));
    const [monday, setMonday] = useState("");
    const [tuesday, setTuesday] = useState("");
    const [wednesday, setWednesday] = useState("");
    const [thursday, setThursday] = useState("");
    const [friday, setFriday] = useState("");
    const [dishes, setDishes] = useState<string[]>([]);
    useEffect(() => {
        // Fetch dish names when the component mounts
        const fetchDishes = async () => {
            const response = await fetch("http://localhost:8080/dishes", {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
                }
            });
            const data = await response.json();
            const dishNames = data.map(dish => dish.description); // Assuming each dish object has a description field
            setDishes(dishNames);
        };

        fetchDishes().then(() => console.log("Fetched dishes")).catch((reason) => console.log(`could not fetch dishes: ${reason}`));
    }, []); // Empty dependency array means this effect runs once on mount


    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const formData = {
            Montag: monday,
            Dienstag: tuesday,
            Mittwoch: wednesday,
            Donnerstag: thursday,
            Freitag: friday,
            validFrom: start.format("YYYY-MM-DD"),
            validTo: end.format("YYYY-MM-DD"),
        };
        console.log(formData);

        fetch("http://localhost:8080/newMenu", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            },
            body: JSON.stringify(formData)
        }).catch((reason) => console.log(`could not save menu: ${reason}`));
    }

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="de">
            <Box component={"form"} onSubmit={handleSubmit} sx={{mt: 1}}>

                <Typography variant={"h2"}>Neues Menü planen</Typography>
                <Grid container spacing={2} sx={{width: '100%', justifyContent: 'space-between'}}>
                    <Grid item style={{flexGrow: 1}}>
                        <DatePicker label={"Start"}
                                    value={start}
                                    onChange={(newValue) => {
                                        setStart(newValue);
                                        setEnd(newValue.add(4, 'day'))

                                    }}
                        />
                    </Grid>
                    <Grid item>
                        <DatePicker label={"Ende"}
                                    value={end}
                                    onChange={(newValue) => setEnd(newValue)}
                        />
                    </Grid>
                </Grid>


                <WeekdayScheduler key={"Montag"} day={"Montag"} handle={(_day, value) => setMonday(value)}
                                  dishes={dishes}/>
                <WeekdayScheduler key={"Dienstag"} day={"Dienstag"} handle={(_day, value) => setTuesday(value)}
                                  dishes={dishes}/>
                <WeekdayScheduler key={"Mittwoch"} day={"Mittwoch"} handle={(_day, value) => setWednesday(value)}
                                  dishes={dishes}/>
                <WeekdayScheduler key={"Donnerstag"} day={"Donnerstag"} handle={(_day, value) => setThursday(value)}
                                  dishes={dishes}/>
                <WeekdayScheduler key={"Freitag"} day={"Freitag"} handle={(_day, value) => setFriday(value)}
                                  dishes={dishes}/>

                <Button variant={"contained"} type={"submit"}>Speichern</Button>
            </Box>
        </LocalizationProvider>
    );
}

function WeekdayScheduler({day, handle, dishes}: {
    day: string,
    handle: (day: string, value: string) => void
    dishes: string[]
}) {

    return (
        <Autocomplete onInputChange={(_e, newVal) => handle(day, newVal)}
                      options={dishes}
                      freeSolo
                      renderInput={(params) =>
                          <TextField {...params}
                                     variant={"outlined"}
                                     margin={"normal"}
                                     fullWidth
                                     label={day}
                                     type={"text"}
                          />}
        />
    )
}