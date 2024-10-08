import {Box, Button, List, ListItem, ListItemIcon, ListItemText, Typography} from "@mui/material";
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import CancelIcon from '@mui/icons-material/Cancel';
import LocalDiningIcon from '@mui/icons-material/LocalDining';

type Order = {
    date: string,
    dish: string,
    location: string,
    status: string,
}

const orders: Order[] = [
    {
        date: "2021-10-11",
        dish: "Spaghetti Bolognese",
        location: "Kantine",
        status: "Bestellt"
    },
    {
        date: "2021-10-12",
        dish: "Käsespätzle",
        location: "Kantine",
        status: "Bestellt"
    },
    {
        date: "2021-10-13",
        dish: "Gyros mit Pommes",
        location: "Außer Haus",
        status: "Geliefert"
    },
    {
        date: "2021-10-14",
        dish: "Pizza",
        location: "Kantine",
        status: "Storniert"
    }
]

export default function MyOrders() {
    return (
        <Box margin={"none"}>
            <Typography variant={"h3"} textAlign={"center"}>Meine Bestellungen</Typography>
            <List>
                {orders.map((order) => <OrderListItem order={order}/>)}
            </List>
        </Box>
    )
}

function OrderListItem({order}: { order: Order }) {
    return (
        <ListItem secondaryAction={<Button disabled={order.status !== "Bestellt"}>stornieren</Button>}>
            <ListItemIcon>
                {order.status === "Bestellt" ? <ShoppingCartIcon/> :
                    order.status === "Geliefert" ? <LocalDiningIcon/> : <CancelIcon/>}
            </ListItemIcon>
            <ListItemText primary={order.dish} secondary={`${order.date}: ${order.location}`} fullWidth/>
        </ListItem>
    )
}