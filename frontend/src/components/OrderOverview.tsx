import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {getUrlFrom, mapDayOfWeek} from "../util/functions.ts";
import {useEffect, useState} from "react";
import {OrderCount, OrderOverviewDta} from "../util/Interfaces.ts";
import dayjs from "dayjs";

export default function OrderOverview() {
    const [overview, setOverview] = useState<OrderOverviewDta>({} as OrderOverviewDta)
    const [locations, setLocations] = useState([])
    const [transformedData, setTransformedData] = useState<Object>({})
    const fetchOverview = async () => {
        let overviewUrl = getUrlFrom("statistics", "order-overview")
        const data = await fetch(overviewUrl + "?" + new URLSearchParams(
            {from: dayjs().format("YYYY-MM-DD")}), {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            }
        })
            .then(resp => {
                if (resp.status === 404) {
                    return fetch(overviewUrl + "?" + new URLSearchParams(
                        {from: dayjs().subtract(3, "days").format("YYYY-MM-DD")}), {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
                        }
                    })
                }
            })
            .then(resp => resp.json())
        setOverview(data)
        setLocations([...new Set(data.orders.map((o, id) => o.location))])
        const transformed = transformOrders(data.orders)
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
                            <TableCell>{day}</TableCell>
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


const transformOrders = (orders: OrderCount[]) => {
    const transformed = {};
    orders.forEach(order => {
        const day = mapDayOfWeek(order.day);
        if (!transformed[day]) {
            transformed[day] = {};
        }
        transformed[day][order.location] = order.orderCount;
    });
    return transformed;
};

