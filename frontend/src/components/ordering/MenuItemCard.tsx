import {Box, Card, CardContent, Typography} from "@mui/material";

function MenuItemCard({day, dish, available}: {
    day: string,
    dish: string,
    available: boolean,

}) {


    return (
        <Box sx={{flexGrow: 1}} key={day}>

            <Card color={"secondary"} sx={{
                ":hover": {boxShadow: 20}
            }}>
                <CardContent>
                    <Typography color={available ? "black" : "lightgrey"}
                                sx={{typography: {xs: 'h6', sm: 'h4'}}}>{day}</Typography>
                    <Typography color={available ? "black" : "lightgrey"}
                                sx={{typography: {xs: 'p', sm: 'h6'}}}>{dish}</Typography>
                </CardContent>

            </Card>
        </Box>
    )
}

export default MenuItemCard