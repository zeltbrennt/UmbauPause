import {Alert, Button, Fade, FormControl, InputAdornment, Stack, TextField, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {formatNumberToCurrency, getUrlFrom} from "../../util/functions.ts";


export default function PriceSetter() {

    const [value, setValue] = useState(0)
    const [displayed, setDisplayed] = useState('')
    const [priceChanged, setPriceChanged] = useState(false)

    const handleSubmit = (event) => {
        event.preventDefault();
        fetch(getUrlFrom("content", `update-price?newPrice=${value}`), {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            }
        }).then(resp => {
            if (resp.status === 200)
                setPriceChanged(true)
        })
            .catch(error => console.error(error))
    }

    useEffect(() => {
        const timer = setTimeout(() => {
            setPriceChanged(false)
        }, 5000)
        return () => clearTimeout(timer)
    }, [priceChanged]);

    return (
        <FormControl component="form" onSubmit={handleSubmit}>
            <Typography variant={"h5"} marginBottom={1}>Preis anpassen</Typography>
            <Stack direction="row" spacing={2} sx={{justifyItems: 'space-between'}} marginBottom={2}>
                <TextField
                    label={"neuer Preis"}
                    required
                    width={"25ch"}
                    value={displayed}
                    onChange={event => {
                        const temp = event.target.value.replace(/[^0-9]+/g, '') as number
                        setValue(temp)
                        setDisplayed((temp !== 0) ? formatNumberToCurrency(temp) : '')
                    }}
                    fullWidth
                    InputProps={{
                        endAdornment: (<InputAdornment position={"end"}>€</InputAdornment>)
                    }}
                />
                <Button type={"submit"} variant={"outlined"}>OK</Button>
            </Stack>
            <Fade in={priceChanged}><Alert severity={"success"}>Preis geändert</Alert></Fade>
        </FormControl>
    )
}