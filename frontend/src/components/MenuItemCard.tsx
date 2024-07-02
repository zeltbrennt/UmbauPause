import {IMenuItem} from "../util/Interfaces.ts";
import {Box, Card, CardContent, Typography} from "@mui/material";

function MenuItemCard({data}: { data: IMenuItem }) {

    return (
        <Box sx={{flexGrow: 1}} key={data.name}>

            <Card sx={{
                ":hover": {boxShadow: 20}
            }}>
                <CardContent>
                    <Typography variant="h3">{data.scheduled}</Typography>
                    <Typography variant="h6">{data.name}</Typography>
                </CardContent>
            </Card>
        </Box>
    )
}

export default MenuItemCard