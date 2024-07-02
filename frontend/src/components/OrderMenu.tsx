import {useEffect, useState} from "react";
import {IMenuItem} from "../util/Interfaces.ts";
import {List, ListItem} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";


function OrderMenu() {

    const api_url = "http://localhost:8080/weekly"

    const [items, setItems] = useState<IMenuItem[]>([])
    const getMenu = async () => {
        const response = await fetch(api_url)
        const data = await response.json()

        console.log(data.map((data: IMenuItem) => data.name))
        setItems(data)

    }

    useEffect(() => {
        getMenu().catch(() => console.log("could not fetch menu items"))
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