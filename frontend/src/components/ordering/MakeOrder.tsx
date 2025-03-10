import {
    Alert,
    Autocomplete,
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    Grid,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import {useEffect, useState} from "react";
import {DeliveryLocation, MenuInfo, MenuItem, Order} from "../../util/Interfaces.ts";
import dayjs from "dayjs";
import {getUrlFrom} from "../../util/functions.ts";

export default function MakeOrder() {

    const [orders, setOrders] = useState<Order[]>([])
    const week = ["Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"]
    const locationUrl = getUrlFrom("info", "locations")
    const [locations, setLocations] = useState<DeliveryLocation[]>([])
    const [menu, setMenu] = useState<MenuInfo>({} as MenuInfo)
    const [validFrom, setValidFrom] = useState("")
    const [validTo, setValidTo] = useState("")

    const [success, setSuccess] = useState(false)
    const [hasOrdered, setHasOrdered] = useState(false)
    const [orderInvalid, setOrderInvalid] = useState(false)

    const getMenu = async () => {
        let menuUrl = getUrlFrom("info", "menu", dayjs().format("YYYY-MM-DD"))
        let menu: MenuInfo = await fetch(menuUrl)
            .then(resp => {
                if (resp.status === 404) {
                    // it's weekend, try preview next week
                    menuUrl = getUrlFrom("info", "menu", dayjs().add(3, 'days').format("YYYY-MM-DD"))
                    return fetch(menuUrl)
                }
                return resp
            })
            .then(resp => resp.json())
            .catch((reason) => console.log(`could not fetch menu: ${reason}`))
        const location = await fetch(locationUrl)
            .then(response => response.json())
            .catch((reason) => console.log(`could not fetch location: ${reason}`))
        setMenu(menu)
        setLocations(location.filter(loc => loc.active))
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

        orders.forEach(o => {
            o.itemName = menu.dishes.find(d => d.id === o.item).name
            o.locationName = locations.find(l => l.id === o.location).name
            o.dayName = week[o.day - 1]
        })
        const completeOrder = {validFrom: menu.validFrom, validTo: menu.validTo, orders: orders}
        console.log(
            completeOrder
        )
        const orderUrl = getUrlFrom("create-checkout-session")
        fetch(orderUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            },
            body: JSON.stringify(completeOrder)
        })
            .then(resp => resp.json())
            .then(data => {
                if (data.redirectUrl) {
                    console.log(data.redirectUrl);
                    window.location.href = data.redirectUrl
                } else {
                    setSuccess(false)
                }
            })
            .catch((reason) => console.log(`could not fetch menu items: ${reason}`))
    }

    function handleSubmit() {
        if (orders.length === 0) {
            console.log("no orders")
            setOrderInvalid(true)
        } else {
            setOrderInvalid(false)
            setHasOrdered(true)
            sendOrder()
        }
    }

    return (
        <Box margin={"none"}>
            <Typography variant="h3" textAlign="center">Bestellen</Typography>
            <Typography textAlign="center">vom {validFrom} bis {validTo} </Typography>
            <Stack spacing={2} sx={{marginBottom: 2}}>
                {orders.map((order, id) => {
                    return (

                        <Card key={id} sx={{width: '100%'}}>
                            <CardContent>
                                <Typography
                                    variant="h6">{week ? "" : week[menu.dishes.find(d => d.id === order.item).day - 1]}</Typography>
                                <Typography> {menu?.dishes.find(d => d.id === order.item).name}</Typography>
                                <Typography
                                    variant="caption">{locations.find(l => order.location === l.id).name}</Typography>
                            </CardContent>
                            <CardActions>
                                <Button
                                    onClick={() => setOrders(orders.filter(o => !(o.item === order.item && o.location === order.location)))}
                                    variant="outlined">entfernen</Button>
                            </CardActions>
                        </Card>


                    )
                })}
            </Stack>
            <Grid container spacing={2}>
                <Grid item sm={6} xs={12}>
                    <Autocomplete
                        options={filterDishesByDay(menu?.dishes, menu.validFrom)}
                        getOptionLabel={(option: MenuItem) => `${week[option.day - 1]}: ${option.name}`}
                        onChange={(event, value) => {
                            setCurrentSelectedMenuItem(value as MenuItem)
                        }}
                        renderInput={(params) => <TextField {...params} size={'small'}/>}>
                    </Autocomplete>
                </Grid>
                <Grid item sm={4} xs={12}>
                    <Autocomplete
                        options={locations}
                        getOptionLabel={(option: DeliveryLocation) => option.name}
                        renderInput={(params) => <TextField {...params} size={'small'}/>}
                        onChange={(event, value: DeliveryLocation) => {
                            setCurrentSelectedLocation(value as DeliveryLocation)
                        }}>
                    </Autocomplete>
                </Grid>
                <Grid item sm={2} xs={12}>
                    <Button fullWidth onClick={() => {
                        if (currentSelectedMenuItem && currentSelectedMenuItem) {
                            setOrders([...orders, {
                                item: currentSelectedMenuItem.id,
                                location: currentSelectedLocation?.id ?? 0,
                                day: currentSelectedMenuItem.day
                            }].sort((a, b) => a.item - b.item))
                        }
                    }} variant={"outlined"} size={"medium"}>Hinzufügen</Button>
                </Grid>
            </Grid>

            <Button fullWidth sx={{marginTop: 2, marginBottom: 2}} onClick={handleSubmit} variant={"contained"}
                    disabled={orders.length === 0}>Bestellung
                abschicken</Button>
            <Stack spacing={2}>
                {orderInvalid ?
                    <Alert severity={"info"}>Bitte wähle mindestens ein Gericht von der Karte</Alert> : null}
            </Stack>

        </Box>
    )
}

function filterDishesByDay(dishes?: MenuItem[], validFrom?: string): MenuItem[] {
    if (!dishes) return []
    console.log("filtering....")
    const day = dayjs().day()
    const cutoff = dayjs().hour(10).minute(30)
    return dishes.filter(d => dayjs(validFrom) > dayjs() ? true : d.day > day || (d.day === day && dayjs().isBefore(cutoff)))
}