import {Box, Button, Grid, TextField, Typography} from "@mui/material";
import {FormEvent, useState} from "react";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs"
import "dayjs/locale/de";


export default function ScheduleMenu() {
    const weekdays = ["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"]
    const [start, setStart] = useState(dayjs().add(1, 'week').day(1));
    const [end, setEnd] = useState(dayjs().add(1, 'week').day(5));
    const [weekdaysData, setWeekdaysData] = useState({
        Montag: "",
        Dienstag: "",
        Mittwoch: "",
        Donnerstag: "",
        Freitag: ""
    });

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const formData = {
            start: start.format("YYYY-MM-DD"),
            end: end.format("YYYY-MM-DD"),
            weekdays: weekdaysData
        };
        console.log(formData);
    };

    const handleWeekdayChange = (day: string, value: string) => {
        setWeekdaysData(prev => ({...prev, [day]: value}));
    };

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

                {weekdays.map((day, index) => {
                    return (
                        <Box key={index}>
                            <WeekdayScheduler day={day} index={index} handle={handleWeekdayChange}/>
                        </Box>
                    )
                })}
                <Button variant={"contained"} type={"submit"}>Speichern</Button>
            </Box>
        </LocalizationProvider>
    );
}

function WeekdayScheduler({day, index, handle}: {
    day: string,
    index: number,
    handle: (day: string, value: string) => void
}) {
    const [active, setActive] = useState(true);

    return (
        <TextField variant={"outlined"}
                   margin={"normal"}
                   fullWidth
                   label={day}
                   type={"text"}
                   key={index}
                   disabled={!active}
                   onChange={(e) => handle(day, e.target.value)}/>


    )
}