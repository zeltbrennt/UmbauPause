import {useEffect, useState} from "react";
import {MenuInfo} from "../../util/Interfaces.ts";
import {Stack, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";
import dayjs from "dayjs";
import {getUrlFrom} from "../../util/functions.ts";
import PriceDisplay from "./PriceDisplay.tsx";


function ShowCurrentMenu() {

    const week = ["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"]
    const [menu, setMenu] = useState<MenuInfo>()
    const [validFrom, setValidFrom] = useState("")
    const [validTo, setValidTo] = useState("")
    const getMenu = async () => {
        let menuUrl = getUrlFrom("info", "menu", dayjs().format("YYYY-MM-DD"))
        let response = await fetch(menuUrl)
        if (response.status === 404) {
            // it's weekend, try preview next week
            menuUrl = getUrlFrom("info", "menu", dayjs().add(3, 'days').format("YYYY-MM-DD"))
            response = await fetch(menuUrl)
        }
        if (response.status === 404) {
            // no preview, try previous week
            menuUrl = getUrlFrom("info", "menu", dayjs().subtract(3, 'days').format("YYYY-MM-DD"))
            response = await fetch(menuUrl)
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
            <PriceDisplay/>
            <Stack spacing={1.5} marginTop={2}>
                {week.map((day, id) => {
                    const dish = menu?.dishes[id].name ?? ""
                    return (

                        <MenuItemCard key={day} day={day} dish={dish}
                                      available={dayjs().day() <= id + 1 && dayjs().day() > 0}/>

                    )
                })}
            </Stack>
        </>
    )
}

export default ShowCurrentMenu