import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Button,
    Stack,
    Switch,
    TextField,
    Typography
} from "@mui/material";
import {getUrlFrom} from "../../util/functions.ts";
import {useEffect, useState} from "react";
import {DeliveryLocation} from "../../util/Interfaces.ts";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

export default function LocationSetter() {

    const [locations, setLocations] = useState<DeliveryLocation[]>([])
    const [newLocation, setNewLocation] = useState('')
    const [change, setChange] = useState(false)

    const getLocation = async () => {
        const location = await fetch(getUrlFrom("info", "locations"))
            .then(response => response.json())
        setLocations(location)
    }

    const addLocation = () => {
        fetch(getUrlFrom("location", "add"), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/text',
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            },
            body: newLocation
        })
    }

    const setLocationState = (location: DeliveryLocation) => {
        console.log(location)
        fetch(getUrlFrom("location", "modify"), {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            },
            body: JSON.stringify(location)
        })
    }


    useEffect(() => {
        getLocation().catch(error => console.error(error))
    }, [change]);

    return (
        <Accordion>
            <AccordionSummary expandIcon={<ExpandMoreIcon/>}>
                <Typography variant={"h5"}>
                    Lieferorte bearbeiten
                </Typography>
            </AccordionSummary>
            {locations.map(location => (

                <AccordionDetails key={location.id}>
                    <Stack direction={"row"} sx={{alignItems: 'center', justifyContent: 'space-between'}}>
                        <Typography>
                            {location.name}
                        </Typography>
                        <Switch defaultChecked={location.active}
                                onChange={() => {
                                    setLocationState({...location, active: !location.active})
                                    setChange(!change)
                                }}
                        />
                    </Stack>
                </AccordionDetails>
            ))}
            <AccordionDetails>
                <Stack direction={"row"} sx={{alignItems: 'center', justifyContent: 'space-between'}} spacing={2}>
                    <TextField fullWidth label={"neuer Lieferort"} value={newLocation}
                               onChange={(event) => setNewLocation(event.target.value)}></TextField>
                    <Button variant={"contained"} onClick={() => {
                        addLocation()
                        setChange(!change)
                    }}
                    >OK</Button>
                </Stack>
            </AccordionDetails>
        </Accordion>
    )
}