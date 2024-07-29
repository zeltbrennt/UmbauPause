import {Button, List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";
import {useEffect, useState} from "react";
import {MenuInfo} from "../util/Interfaces.ts";
import dayjs from "dayjs";

export default function MakeOrder() {

    const [orders, setOrders] = useState<number[]>([])
    const [selected, setSelected] = useState([false, false, false, false, false])
    const week = ["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"]
    const api_url = "http://localhost:8080/menu?" // TODO: URL parametrisieren, da sie in der Docker Umgebung auf das Host System verweist
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

    const handleClick = () => {
        console.log(
            orders
        )
        fetch("http://localhost:8080/order", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            },
            body: JSON.stringify(orders)
        }).catch((reason) => console.log(`could not fetch menu items: ${reason}`))
    }

    return (
        <>
            <Typography variant={"h3"} textAlign={"center"}>Bestellen</Typography>
            <Typography textAlign={"center"}>vom {validFrom} bis {validTo} </Typography>
            <List disablePadding>
                {week.map((day, id) => {
                    const dish = menu?.dishes[id].name ?? ""
                    const menuItemId = menu?.dishes[id].id ?? 0
                    return (
                        <ListItem key={id} sx={{flexGrow: 1}}>
                            <MenuItemCard day={day} dish={dish} handleClick={() => {
                                const newSelected = [...selected]
                                newSelected[id] = !newSelected[id]
                                setSelected(newSelected)
                                if (newSelected[id]) {
                                    setOrders([...orders, menuItemId])
                                } else {
                                    setOrders(orders.filter(order => order !== menuItemId))
                                }

                            }}/>
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

