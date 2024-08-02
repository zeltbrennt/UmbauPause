import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {mapDayOfWeek} from "../util/functions.ts";
import {useEffect, useState} from "react";
import {OrderOverviewDta} from "../util/Interfaces.ts";
import dayjs from "dayjs";

export default function OrderOverview() {
    const [overview, setOverview] = useState<OrderOverviewDta>({} as OrderOverviewDta)
    const [locations, setLocations] = useState([])
    const [transformedData, setTransformedData] = useState()
    const fetchOverview = async () => {
        const response = await fetch("http://localhost:8080/orderOverview?" + new URLSearchParams(
            {from: dayjs().format("YYYY-MM-DD")}))
        const data: OrderOverviewDta = await response.json()
        console.log(data)
        setOverview(data)
        setLocations([...new Set(data.orders.map((o, id) => o.location))])
        const transformed: any = transformOrders(data.orders)
        console.log(transformed)
        setTransformedData(transformed)
    }
    useEffect(() => {
        fetchOverview().catch((reason) => console.log(`could not fetch order overview: ${reason}`))
    }, [])
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
                    {Object.keys(transformedData).map((day, id) => (
                        <TableRow key={id}>
                            <TableCell>{mapDayOfWeek(parseInt(day))}</TableCell>
                            {locations.map((location, id) => (
                                <TableCell key={id}>{transformedData[day][location] ?? 0}</TableCell>
                            ))}
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    )
}


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

