import {Button, List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";
import {useEffect, useState} from "react";
import {IWeeklyMenu} from "../util/Interfaces.ts";

export default function MakeOrder() {

    const [monday, setMonday] = useState(false);
    const [tuesday, setTuesday] = useState(false);
    const [wednesday, setWednesday] = useState(false);
    const [thursday, setThursday] = useState(false);
    const [friday, setFriday] = useState(false);
    const [menu, setMenu] = useState({} as IWeeklyMenu)
    const api_url = "http://localhost:8080/current_menu"
    const getMenu = async () => {
        const response = await fetch(api_url)
        const data = await response.json()
        setMenu(data)
    }

    useEffect(() => {
        getMenu().catch((reason) => console.log(`could not fetch menu items: ${reason}`))
    }, [])

    const handleClick = () => {
        const order = {
            validFrom: menu.validFrom,
            validTo: menu.validTo,
            Montag: monday,
            Dienstag: tuesday,
            Mittwoch: wednesday,
            Donnerstag: thursday,
            Freitag: friday
        } as IWeeklyMenu
        console.log(order)
        fetch("http://localhost:8080/order", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}`
            },
            body: JSON.stringify(order)
        }).catch((reason) => console.log(`could not post order: ${reason}`))
    }
    return (
        <>
            <Typography variant={"h3"} textAlign={"center"}>Bestellen</Typography>
            <Typography textAlign={"center"}>vom {menu.validFrom} bis {menu.validTo} </Typography>
            <List disablePadding>
                <ListItem>
                    <MenuItemCard day={"Montag"} dish={menu.Montag} handleClick={() => setMonday(true)}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Dienstag"} dish={menu.Dienstag} handleClick={() => setTuesday(!tuesday)}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Mittwoch"} dish={menu.Mittwoch} handleClick={() => setWednesday(!wednesday)}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Donnerstag"} dish={menu.Donnerstag} handleClick={() => setThursday(!thursday)}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Freitag"} dish={menu.Freitag} handleClick={() => setFriday(!friday)}/>
                </ListItem>
                <ListItem sx={{justifyContent: "right"}}>
                    <Button variant="contained" onClick={handleClick}>Bestellen</Button>
                </ListItem>
            </List>
        </>
    )
}

