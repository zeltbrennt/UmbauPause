import {useEffect, useState} from "react";
import {IMenuItem} from "../util/Interfaces.ts";
import {List, ListItem} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";


function OrderMenu() {

    const api_url = "http://localhost:8080/weekly"

    const [items, setItems] = useState<IMenuItem[]>([])
    const getMenu = async () => {
        const response = await fetch(api_url, {
            headers: {
                "Authorization": `Bearer ${import.meta.env.VITE_API_TOKEN}`
            }
        })
        const data = await response.json()
        setItems(data)
    }

    useEffect(() => {
        getMenu().catch((reason) => console.log(`could not fetch menu items: ${reason}`))
    }, [])
    return (
        <List>
            {items.map((item) => (
                <ListItem key={item.name} sx={{flexGrow: 1}}>
                    <MenuItemCard data={item}/>
                </ListItem>
            ))}
        </List>
    )
}

export default OrderMenu