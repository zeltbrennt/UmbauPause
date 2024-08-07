import {Box, Card, CardContent, Typography, useTheme} from "@mui/material";
import {MenuItemState} from "../../util/Interfaces.ts";
import {useState} from "react";

function MenuItemCard({day, dish, available}: {
    day: string,
    dish: string,
    available: boolean,

}) {

    const [selection, setSelection] = useState(MenuItemState.AVAILABLE)
    const toggleSelection = () => {
        console.log(selection.toString())
        setSelection(selection === MenuItemState.SELECTED ? MenuItemState.AVAILABLE : MenuItemState.SELECTED)
    }
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

function getCardColor(state: MenuItemState) {
    const theme = useTheme()
    switch (state) {
        case MenuItemState.AVAILABLE:
            return theme.palette.background.default
        case MenuItemState.UNAVAILABLE:
            return theme.palette.action.disabled
        case MenuItemState.SELECTED:
            return theme.palette.primary.main
    }
}

export default MenuItemCard