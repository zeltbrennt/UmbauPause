import {useEffect, useState} from "react";
import {IMenuItem} from "../util/Interfaces.ts";
import {List, ListItem} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";


function OrderMenu() {

    const api_url = "http://localhost:8080/weekly" // TODO: URL parametrisieren, da sie in der Docker Umgebung auf das Host System verweist

    const [items, setItems] = useState<IMenuItem[]>([])
    const getMenu = async () => {
        const response = await fetch(api_url)
        const data = await response.json()
        setItems(data)
    }

    useEffect(() => {
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