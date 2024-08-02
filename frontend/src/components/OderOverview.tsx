import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {mapDayOfWeek} from "../util/functions.ts";

export default function OrderOverview() {
    console.log(transOverview)
    return (
        <TableContainer>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell></TableCell>
                        {locations.map((o, id) => (
                            <TableCell key={id}>{o}</TableCell>
                        ))}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {Object.keys(transOverview).map((day, id) => (
                        <TableRow key={id}>
                            <TableCell>{mapDayOfWeek(parseInt(day))}</TableCell>
                            {locations.map((location, id) => (
                                <TableCell key={id}>{transOverview[day][location] ?? 0}</TableCell>
                            ))}
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    )
}

const overview = {
    "validFrom": "2024-07-29",
    "validTo": "2024-08-02",
    "timestamp": "2024-08-02T12:53:55.276+02:00",
    "orders": [
        {
            "day": 1,
            "location": "Haupthaus",
            "orders": 2
        },
        {
            "day": 2,
            "location": "e-werk",
            "orders": 1
        },
        {
            "day": 2,
            "location": "Haupthaus",
            "orders": 1
        },
        {
            "day": 3,
            "location": "Haupthaus",
            "orders": 1
        }
    ]
}

const locations = [...new Set(overview.orders.map((o, id) => o.location))]

const transformOrders = (orders) => {
    const transformed = {};
    orders.forEach(order => {
        if (!transformed[order.day]) {
            transformed[order.day] = {};
        }
        transformed[order.day][order.location] = order.orders;
    });
    return transformed;
};

const transOverview = transformOrders(overview.orders);