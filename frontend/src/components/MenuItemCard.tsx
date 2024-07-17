import {Box, Card, CardContent, Typography} from "@mui/material";

function MenuItemCard({day, dish}: { day: string, dish: string }) {

    return (
        <Box sx={{flexGrow: 1}} key={day}>

            <Card sx={{
                ":hover": {boxShadow: 20}
            }}>
                <CardContent>
                    <Typography sx={{typography: {xs: 'h6', sm: 'h4'}}}>{day}</Typography>
                    <Typography sx={{typography: {xs: 'p', sm: 'h6'}}}>{dish}</Typography>
                </CardContent>
            </Card>
        </Box>
    )
}

export default MenuItemCard