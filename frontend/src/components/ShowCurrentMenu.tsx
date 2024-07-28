import {useEffect, useState} from "react";
import {MenuInfo} from "../util/Interfaces.ts";
import {List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";
import dayjs from "dayjs";


function ShowCurrentMenu() {

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
    return (
        <>
            <Typography variant={"h3"} textAlign={"center"}>Wochenkarte</Typography>
            <Typography
                textAlign={"center"}>vom {validFrom} bis {validTo}</Typography>
            <List>
                {menu?.dishes.map(dish => {
                    return (
                        <ListItem key={dish.id} sx={{flexGrow: 1}}>
                            <MenuItemCard day={dish.day} dish={dish.name}/>
                        </ListItem>
                    )
                })}
            </List>
        </>
    )
}

export default ShowCurrentMenu