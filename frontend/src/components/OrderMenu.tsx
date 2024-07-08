import {useEffect, useState} from "react";
import {IMenuItem} from "../util/Interfaces.ts";
import {List, ListItem} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";

function OrderMenu() {

    const weekly_menu_url = `http://${import.meta.env.VITE_BACKEND_URL}/weekly`
    // TODO: URL parametrisieren, da sie in der Docker Umgebung auf das Host System verweist

    const [items, setItems] = useState<IMenuItem[]>([])
    const getMenu = async () => {
        const response = await fetch(weekly_menu_url, {
            headers: {
                'AccessControlAllowOrigin': '*'
            }
        })
        const data = await response.json()
        setItems(data)
    }

    useEffect(() => {
        console.log(`base url: ${import.meta.env.BASE_URL}`)
        console.log(`dev: ${import.meta.env.DEV}`)
        console.log(`VITE_BACKEND: ${import.meta.env.VITE_BACKEND_URL}`)
        console.log(`fetch from: ${weekly_menu_url}`)
        getMenu().catch((reason) => console.log(`could not fetch menu items: ${reason}`))
    }, [])
    return (
        <List>
            {items.map((item) => (
                <ListItem key={item.description} sx={{flexGrow: 1}}>
                    <MenuItemCard data={item}/>
                </ListItem>
            ))}
        </List>
    )
}

export default OrderMenu