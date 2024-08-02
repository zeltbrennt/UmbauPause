import {Autocomplete, Box, Button, Grid, List, ListItem, ListItemText, TextField, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {DeliveryLocation, MenuInfo, MenuItem, Order} from "../util/Interfaces.ts";
import dayjs from "dayjs";

export default function MakeOrder() {

    const [orders, setOrders] = useState<Order[]>([])
    const [selected, setSelected] = useState([false, false, false, false, false])
    const week = ["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"]
    const locationUrl = "http://localhost:8080/location"
    const [locations, setLocations] = useState<DeliveryLocation[]>([])
    const menuUrl = "http://localhost:8080/menu?" // TODO: URL parametrisieren, da sie in der Docker Umgebung auf das Host System verweist
    const [menu, setMenu] = useState<MenuInfo>()
    const [validFrom, setValidFrom] = useState("")
    const [validTo, setValidTo] = useState("")
    const getMenu = async () => {
        const menu: MenuInfo = await fetch(menuUrl + new URLSearchParams(
            {from: dayjs().format("YYYY-MM-DD")}))
            .then(response => response.json())
            .catch((reason) => console.log(`could not fetch menu: ${reason}`))
        const location = await fetch(locationUrl)
            .then(response => response.json())
            .catch((reason) => console.log(`could not fetch location: ${reason}`))
        setMenu(menu)
        setLocations(location)
        setValidFrom(dayjs(menu.validFrom).format("DD.MM.YYYY"))
        setValidTo(dayjs(menu.validTo).format("DD.MM.YYYY"))
    }

    useEffect(() => {
        getMenu().catch((reason) => console.log(`could not fetch menu items: ${reason}`))
        console.log(locations)
    }, [])

    const [currentSelectedMenuItem, setCurrentSelectedMenuItem] = useState<MenuItem>()

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
        <Box>
            <Typography variant={"h3"} textAlign={"center"}>Bestellen</Typography>
            <Typography textAlign={"center"}>vom {validFrom} bis {validTo} </Typography>
            <List>
                {orders.map((order, id) => {
                    return (
                        <ListItem key={id}>
                            <ListItemText primary={`Day: ${order.item} Location: ${order.location}`}/>
                            <Button
                                onClick={() => setOrders(orders.filter(o => o.item !== order.item && o.location !== order.location))}
                                variant={"outlined"}>remove</Button>
                        </ListItem>
                    )
                })}
            </List>
            <Grid container spacing={2}>
                <Grid sm={6}>
                    <Autocomplete
                        options={[{id: 1, name: "Dish A", day: 1} as MenuItem, {
                            id: 2,
                            name: "Dish B",
                            day: 2
                        } as MenuItem]}
                        getOptionLabel={(option: MenuItem) => `Day: ${option.day}: ${option.name}`}
                        onChange={(event, value) => {
                            setCurrentSelectedMenuItem(value as MenuItem)
                        }}
                        renderInput={(params) => <TextField {...params}/>}>
                    </Autocomplete>
                </Grid>
                <Grid sm={4}>
                    <Autocomplete
                        options={[{id: 1, name: "Location A"}, {id: 2, name: "Location B"}]}
                        getOptionLabel={(option: DeliveryLocation) => option.name}
                        renderInput={(params) => <TextField {...params}/>}
                        onChange={(event, value: DeliveryLocation) => {
                            if ("id" in currentSelectedMenuItem) {
                                setOrders([...orders, {item: currentSelectedMenuItem.id, location: value.id}])
                            }
                            setCurrentSelectedMenuItem(null)
                        }}>
                    </Autocomplete>
                </Grid>

            </Grid>
            <Button onClick={() => console.log(orders)} variant={"outlined"}>Add</Button>
        </Box>
    )
}

