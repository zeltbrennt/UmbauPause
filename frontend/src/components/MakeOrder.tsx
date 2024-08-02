import {
    Autocomplete,
    Box,
    Button,
    Card,
    CardActionArea,
    CardContent,
    Grid,
    List,
    ListItem,
    TextField,
    Typography
} from "@mui/material";
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
    const [currentSelectedLocation, setCurrentSelectedLocation] = useState<DeliveryLocation>()

    const sendOrder = () => {
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

                        <ListItem>
                            <Card>
                                <CardContent>
                                    <Typography
                                        variant={"h6"}>{week[menu?.dishes.find(d => d.id === order.item).day - 1]}</Typography>
                                    <Typography> {menu?.dishes.find(d => d.id === order.item).name}</Typography>
                                    <Typography
                                        variant={"caption"}>{locations.find(l => order.location === l.id).name}</Typography>
                                </CardContent>
                                <CardActionArea>
                                    <Button
                                        onClick={() => setOrders(orders.filter(o => o.item !== order.item))}
                                        variant={"outlined"}>entfernen</Button>
                                </CardActionArea>
                            </Card>
                        </ListItem>


                    )
                })}
            </List>
            <Grid container spacing={2}>
                <Grid item sm={6}>
                    <Autocomplete
                        options={menu?.dishes ?? []}
                        getOptionLabel={(option: MenuItem) => `${week[option.day - 1]}: ${option.name}`}
                        onChange={(event, value) => {
                            setCurrentSelectedMenuItem(value as MenuItem)
                        }}
                        renderInput={(params) => <TextField {...params}/>}>
                    </Autocomplete>
                </Grid>
                <Grid item sm={4}>
                    <Autocomplete
                        options={locations}
                        getOptionLabel={(option: DeliveryLocation) => option.name}
                        renderInput={(params) => <TextField {...params}/>}
                        onChange={(event, value: DeliveryLocation) => {
                            setCurrentSelectedLocation(value as DeliveryLocation)
                        }}>
                    </Autocomplete>
                </Grid>
                <Grid item sm={2}>
                    <Button onClick={() => {
                        if (currentSelectedMenuItem && currentSelectedMenuItem) {
                            setOrders([...orders, {
                                item: currentSelectedMenuItem.id,
                                location: currentSelectedLocation?.id ?? 0
                            }].sort((a, b) => a.item - b.item))
                        }
                    }} variant={"outlined"}>Hinzufügen</Button>
                </Grid>
            </Grid>
            <Button onClick={sendOrder} variant={"outlined"}>Bestellung abschicken</Button>
        </Box>
    )
}

