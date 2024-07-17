import {useEffect, useState} from "react";
import {IWeeklyMenu} from "../util/Interfaces.ts";
import {List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";


function ShowCurrentMenu() {

    const api_url = "http://localhost:8080/current_menu" // TODO: URL parametrisieren, da sie in der Docker Umgebung auf das Host System verweist

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
                <ListItem key={"monday"} sx={{flexGrow: 1}}>
                    <MenuItemCard day={"Montag"} dish={menu.monday}/>
                </ListItem>
                <ListItem key={"tuesday"} sx={{flexGrow: 1}}>
                    <MenuItemCard day={"Dienstag"} dish={menu.tuesday}/>
                </ListItem>
                <ListItem key={"wednesday"} sx={{flexGrow: 1}}>
                    <MenuItemCard day={"Mittwoch"} dish={menu.wednesday}/>
                </ListItem>
                <ListItem key={"thursday"} sx={{flexGrow: 1}}>
                    <MenuItemCard day={"Donnerstag"} dish={menu.thursday}/>
                </ListItem>
                <ListItem key={"friday"} sx={{flexGrow: 1}}>
                    <MenuItemCard day={"Freitag"} dish={menu.friday}/>
                </ListItem>

            </List>
        </>
    )
}

export default ShowCurrentMenu