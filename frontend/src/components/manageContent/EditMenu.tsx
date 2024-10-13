import {Alert, Autocomplete, Button, Fade, Stack, TextField, Typography} from "@mui/material";
import {FormEvent, useEffect, useState} from "react";
import {LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs"
import "dayjs/locale/de";
import {getUrlFrom} from "../../util/functions.ts";
import {MenuInfo, MenuItem} from "../../util/Interfaces.ts";


export default function EditMenu() {
    const [end, setEnd] = useState("");
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

    const getMenu = async () => {
        try {
            let menuUrl = getUrlFrom("info", "menu", dayjs().format("YYYY-MM-DD"))
            let response = await fetch(menuUrl)
            const data: MenuInfo = await response.json()
            setEnd(data.validTo)
            setNewDishes(data.dishes.map(dish => dish.name))
            console.log(newDishes)
        } catch (e) {
            console.error(`could not fetch menu: ${e}`)
        }
        //setValidFrom(dayjs(data.validFrom).format("DD.MM.YYYY"))
        //setValidTo(dayjs(data.validTo).format("DD.MM.YYYY"))
    }

    useEffect(() => {
        getMenu()
        fetchDishes()
    }, []);

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
        console.log(`start: ${dayjs()}, end: ${end}`)
        console.log(`new dishes: ${newDishes}`)
        const nextDishes: MenuItem[] = newDishes.map((dish, index) => ({id: 0, name: dish, day: index + 1} as MenuItem))
        const formData: MenuInfo = {
            validFrom: dayjs().format("YYYY-MM-DD"),
            validTo: end,
            dishes: nextDishes
        };
        console.log("new menu is this: " + formData.dishes);

        let menuUrl = getUrlFrom("content", "edit-menu")
        fetch(menuUrl, {
            method: 'PUT',
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

                <Typography variant={"h2"}>Aktuelles Menu bearbeiten</Typography>

                {["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"].map((day, idx) => (
                    <WeekdayScheduler key={day}
                                      day={day}
                                      defaultValue={newDishes[idx] ?? "something went wrong..."}
                                      handleChange={(newValue: string) => {
                                          const temp = [...newDishes]
                                          temp[idx] = newValue
                                          setNewDishes(temp)
                                      }}
                                      dishes={dishes}/>))}


                <Button variant={"contained"} type={"submit"} sx={{mt: 2, mb: 3}}>Speichern</Button>
                {submitted && <Fade in={submitted}>
                    <Alert severity="success">Menü aktualisiert</Alert>
                </Fade>}
                {error && <Fade in={error}>
                    <Alert severity="error">Fehler beim Speichern</Alert>
                </Fade>}
            </Stack>
        </LocalizationProvider>
    );
}


function WeekdayScheduler({day, handleChange, dishes, defaultValue}: {
    day: string,
    handleChange: (value: string) => void
    dishes: string[]
    defaultValue: string
}) {

    return (
        <Autocomplete onInputChange={(_e, newVal) => handleChange(newVal)}
                      options={dishes}
                      freeSolo
                      value={defaultValue}
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