import {
    Autocomplete,
    Box,
    Card,
    CardActions,
    CardContent,
    FormControlLabel,
    Switch,
    TextField,
    Typography,
    useTheme
} from "@mui/material";
import {DeliveryLocation, MenuItemState} from "../util/Interfaces.ts";
import {useState} from "react";
import Grid2 from "@mui/material/Unstable_Grid2";

function MenuItemCard({day, dish, handleClick, locations}: {
    day: string,
    dish: string,
    handleClick?: () => void,
    locations?: DeliveryLocation[]
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
            }} onClick={() => {
                if (handleClick) {
                    toggleSelection();
                    handleClick()
                }
            }}>
                <CardContent>
                    <Typography sx={{typography: {xs: 'h6', sm: 'h4'}}}>{day}</Typography>
                    <Typography sx={{typography: {xs: 'p', sm: 'h6'}}}>{dish}</Typography>
                </CardContent>
                {
                    handleClick ?
                        <CardActions>
                            <Grid2>

                                <Autocomplete
                                    options={locations}
                                    getOptionLabel={(option) => option.name}
                                    defaultValue={locations ? locations[0] : null}
                                    sx={{width: "100%"}}
                                    renderInput={params => <TextField
                                        {...params}
                                        label="Lieferort"
                                        variant="standard"
                                        defaultValue={locations ? locations[0] : "asdf"}
                                        fullWidth
                                    />}>
                                </Autocomplete>

                                <FormControlLabel
                                    sx={{marginLeft: "auto"}}
                                    control={<Switch onChange={handleClick}></Switch>}
                                    label={"Auswählen"}
                                    labelPlacement={"start"}
                                >
                                </FormControlLabel>
                            </Grid2>

                        </CardActions>
                        : null
                }
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