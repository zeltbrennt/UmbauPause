import {useEffect, useState} from "react";
import {IWeeklyMenu} from "../util/Interfaces.ts";
import {List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";


function ShowCurrentMenu() {

    const api_url = "http://localhost:8080/current_menu" // TODO: URL parametrisieren, da sie in der Docker Umgebung auf das Host System verweist
    const weekdays = ["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"]
    const [menu, setMenu] = useState({} as IWeeklyMenu)
    const getMenu = async () => {
        const response = await fetch(api_url)
        const data = await response.json()
        setMenu(data)
    }

    useEffect(() => {
        getMenu().catch((reason) => console.log(`could not fetch menu items: ${reason}`))
    }, [])
    return (
        <>
            <Typography variant={"h3"} textAlign={"center"}>Wochenkarte</Typography>
            <Typography textAlign={"center"}>vom {menu.validFrom} bis {menu.validTo}</Typography>
            <List>
                {weekdays.map(day => {
                    return (
                        <ListItem key={day} sx={{flexGrow: 1}}>
                            <MenuItemCard day={day} dish={menu[day]}/>
                        </ListItem>
                    )
                })}
            </List>
        </>
    )
}

export default ShowCurrentMenu