import {Box, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {TagCount} from "../../util/Interfaces.ts";
import {getUrlFrom} from "../../util/functions.ts";
import {BarChart} from "@mui/x-charts";

export default function TagStatistics() {

    const [stats, setStats] = useState<TagCount[]>([])

    const foo: TagCount[] = [
        {
            tag: "foo",
            count: 42,
        },
        {
            tag: "bar",
            count: 1337,
        }
    ]

    useEffect(() => {
        const fetchTags = async () => {
            const url = getUrlFrom("statistics", "tags")
            const data = await fetch(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
                }
            })
                .then(resp => resp.json())
            setStats(data)
            console.log(data)
        }
        fetchTags().catch((reason) => console.log(`could not fetch tag statistics: ${reason}`))
    }, []);

    return (
        <Box>
            <Typography variant="h4">Tag-Statistik</Typography>
            <Typography variant="body1">Hier könnte Ihre Tag-Statistik stehen</Typography>
            <BarChart
                yAxis={[{scaleType: 'band', data: stats.map((s) => s.tag)}]}
                series={[{data: stats.map((s) => s.count), label: 'Anzahl'}]}
                layout={"horizontal"}
                width={800}
                height={600}
            />
        </Box>
    )
}