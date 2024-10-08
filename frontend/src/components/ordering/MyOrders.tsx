import {
    Box,
    Button,
    FormControlLabel,
    FormGroup,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Switch,
    Typography
} from "@mui/material";
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import CancelIcon from '@mui/icons-material/Cancel';
import LocalDiningIcon from '@mui/icons-material/LocalDining';
import {useEffect, useState} from "react";
import {getUrlFrom} from "../../util/functions.ts";

type Order = {
    date: string,
    dish: string,
    location: string,
    status: string,
}


export default function MyOrders() {

    const [orders, setOrders] = useState<Order[]>([])
    const [displayAll, setDisplayAll] = useState(false)

    useEffect(() => {
        fetch(getUrlFrom("myorders"),
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}`
                }
            })
            .then(response => response.json())
            .then(data => setOrders(data))
            .then(() => console.log(orders))
            .catch(reason => console.log(`could not fetch orders: ${reason}`))
    }, [])

    return (
        <Box margin={"none"}>
            <Typography variant={"h3"} textAlign={"center"}>Meine Bestellungen</Typography>
            <FormGroup>
                <FormControlLabel label={"Alle anzeigen"} control={
                    <Switch checked={displayAll}
                            onChange={(e) => setDisplayAll(!displayAll)}/>}
                />
            </FormGroup>
            <List>
                {orders
                    .filter(x => x.status === "OPEN" || displayAll)
                    .map((order, id) => <OrderListItem id={id} order={order}/>)}
            </List>
        </Box>
    )
}

function OrderListItem({order, id}: { order: Order, id: number }) {
    return (
        <ListItem key={id} secondaryAction={<Button disabled={order.status !== "OPEN"}>stornieren</Button>}>
            <ListItemIcon>
                {order.status === "OPEN" ? <ShoppingCartIcon/> :
                    order.status === "DELIVERED" ? <LocalDiningIcon/> : <CancelIcon/>}
            </ListItemIcon>
            <ListItemText primary={order.dish} secondary={`${order.date}: ${order.location}`}/>
        </ListItem>
    )
}