import {useEffect, useState} from "react";
import {IMenuItem} from "../util/Interfaces.ts";
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
        getMenu()
    }, [])
    return (
        <div>
            {items.map((item) => <MenuItemCard data={item}/>)}
        </div>
    )
}

export default OrderMenu