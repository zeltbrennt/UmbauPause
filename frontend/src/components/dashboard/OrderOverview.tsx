import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {getUrlFrom, mapDayOfWeek} from "../../util/functions.ts";
import {useEffect, useState} from "react";
import {OrderCount, OrderOverviewDta} from "../../util/Interfaces.ts";
import dayjs from "dayjs";


export default function OrderOverview() {
    const [overview, setOverview] = useState<OrderOverviewDta>({} as OrderOverviewDta)
    const [locations, setLocations] = useState<string[]>([])
    const [transformedData, setTransformedData] = useState<Object>({})
    const fetchOverview = async () => {
        let overviewUrl = getUrlFrom("statistics", "order-overview", dayjs().format("YYYY-MM-DD"))
        const data = await fetch(overviewUrl, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
            }
        })
            .then(resp => {
                console.log("found orders")
                if (resp.status === 404) {
                    overviewUrl = getUrlFrom("statistics", "order-overview", dayjs().subtract(3, 'days').format("YYYY-MM-DD"))
                    return fetch(overviewUrl, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
                        }
                    })
                }
                return resp

            })
            .then(resp => resp.json())
        setOverview(data)
        setLocations([...new Set(data.orders.map((o: any) => o.location))])
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
    const transformed: any = {};
    orders.forEach(order => {
        const day = mapDayOfWeek(order.day);
        if (!transformed[day]) {
            transformed[day] = {};
        }
        transformed[day][order.location] = order.orderCount;
    });
    return transformed;
};

