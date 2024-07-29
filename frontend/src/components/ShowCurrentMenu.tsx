import {useEffect, useState} from "react";
import {MenuInfo} from "../util/Interfaces.ts";
import {List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";
import dayjs from "dayjs";


function ShowCurrentMenu() {

    const api_url = "http://localhost:8080/menu?" // TODO: URL parametrisieren, da sie in der Docker Umgebung auf das Host System verweist
    const week = ["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"]
    const [menu, setMenu] = useState<MenuInfo>()
    const [validFrom, setValidFrom] = useState("")
    const [validTo, setValidTo] = useState("")
    const getMenu = async () => {
        const response = await fetch(api_url + new URLSearchParams(
            {from: dayjs().format("YYYY-MM-DD")}))
        const data: MenuInfo = await response.json()
        setMenu(data)
        console.log(data)
        setValidFrom(dayjs(data.validFrom).format("DD.MM.YYYY"))
        setValidTo(dayjs(data.validTo).format("DD.MM.YYYY"))
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
                {week.map((day, id) => {
                    const dish = menu?.dishes[id].name ?? "Kantine geschlossen"
                    return (
                        <ListItem key={id} sx={{flexGrow: 1}}>
                            <MenuItemCard day={day} dish={dish}/>
                        </ListItem>
                    )
                })}
            </List>
        </>
    )
}

export default ShowCurrentMenu