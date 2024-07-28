import {Button, List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";
import {useEffect, useState} from "react";
import {MenuInfo} from "../util/Interfaces.ts";
import dayjs from "dayjs";

export default function MakeOrder() {

    const [monday, setMonday] = useState(false);
    const [tuesday, setTuesday] = useState(false);
    const [wednesday, setWednesday] = useState(false);
    const [thursday, setThursday] = useState(false);
    const [friday, setFriday] = useState(false);
    const api_url = "http://localhost:8080/current_menu" // TODO: URL parametrisieren, da sie in der Docker Umgebung auf das Host System verweist
    const [menu, setMenu] = useState<MenuInfo>()
    const [validFrom, setValidFrom] = useState("")
    const [validTo, setValidTo] = useState("")
    const getMenu = async () => {
        const response = await fetch(api_url)
        const data: MenuInfo = await response.json()
        setMenu(data)
        setValidFrom(dayjs(data.validFrom).add(1, "day").format("DD.MM.YYYY"))
        setValidTo(dayjs(data.validTo).subtract(1, "day").format("DD.MM.YYYY"))
    }

    useEffect(() => {
        getMenu().catch((reason) => console.log(`could not fetch menu items: ${reason}`))
    }, [])

    const handleClick = () => {

    }
    return (
        <>
            <Typography variant={"h3"} textAlign={"center"}>Bestellen</Typography>
            <Typography textAlign={"center"}>vom {validFrom} bis {validTo} </Typography>
            <List disablePadding>
                {menu?.dishes.map(dish => {
                    return (
                        <ListItem key={dish.id} sx={{flexGrow: 1}}>
                            <MenuItemCard day={dish.day} dish={dish.name} handleClick={handleClick}/>
                        </ListItem>
                    )
                })}
                <ListItem sx={{justifyContent: "right"}}>
                    <Button variant="contained" onClick={handleClick}>Bestellen</Button>
                </ListItem>
            </List>
        </>
    )
}

