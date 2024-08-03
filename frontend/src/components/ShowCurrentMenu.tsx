import {useEffect, useState} from "react";
import {MenuInfo} from "../util/Interfaces.ts";
import {List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";
import dayjs from "dayjs";
import {getUrlFrom} from "../util/functions.ts";


function ShowCurrentMenu() {

    const menuUrl = getUrlFrom("info", "menu")
    const week = ["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"]
    const [menu, setMenu] = useState<MenuInfo>()
    const [validFrom, setValidFrom] = useState("")
    const [validTo, setValidTo] = useState("")
    const getMenu = async () => {
        let response = await fetch(menuUrl + "?" + new URLSearchParams(
            {from: dayjs().format("YYYY-MM-DD")}))
        if (response.status === 404) {
            // it's weekend, try preview next week
            response = await fetch(menuUrl + "?" + new URLSearchParams(
                {from: dayjs().add(3, 'days').format("YYYY-MM-DD")}))
        }
        if (response.status === 404) {
            // no preview, try previous week
            response = await fetch(menuUrl + "?" + new URLSearchParams(
                {from: dayjs().subtract(3, 'days').format("YYYY-MM-DD")}))
        }
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
                    const dish = menu?.dishes[id].name ?? ""
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