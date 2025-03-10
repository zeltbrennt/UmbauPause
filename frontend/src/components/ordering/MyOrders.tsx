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
    id: string,
    date: string,
    dish: string,
    location: string,
    status: string,
}


export default function MyOrders() {

    const [orders, setOrders] = useState<Order[]>([])
    const [displayAll, setDisplayAll] = useState(false)

    const cancelOrder = (orderId: string) => {
        orders.find(order => order.id === orderId)!.status = "CANCELED"
        setOrders([...orders])
        fetch(getUrlFrom("order", "cancel", orderId), {
            method: 'PATCH',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}`
            }
        }).catch(reason => console.log(`could not cancel order: ${reason}`))
    }

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
                    .filter(order => order.status === "OPEN" || displayAll)
                    .map((order) => <OrderListItem handleClick={cancelOrder} order={order}/>)}
            </List>
        </Box>
    )
}

function OrderListItem({order, handleClick}: { order: Order, handleClick: (id: string) => void }) {
    return (
        <ListItem key={order.id} secondaryAction={
            <Button disabled={order.status !== "OPEN"} onClick={() => handleClick(order.id)}>stornieren</Button>}>
            <ListItemIcon>
                {order.status === "OPEN" ? <ShoppingCartIcon/> :
                    order.status === "DELIVERED" ? <LocalDiningIcon/> : <CancelIcon/>}
            </ListItemIcon>
            <ListItemText primary={order.dish} secondary={`${order.date}: ${order.location}`}/>
        </ListItem>
    )
}