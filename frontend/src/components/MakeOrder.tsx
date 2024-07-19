import {List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";

export default function MakeOrder() {
    return (
        <>
            <Typography variant={"h3"} textAlign={"center"}>Bestellen</Typography>
            <Typography textAlign={"center"}>vom ??? bis ??? </Typography>
            <List>
                <ListItem>
                    <MenuItemCard day={"Montag"} dish={"whatever"}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Dienstag"} dish={"whatever"}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Mittwoch"} dish={"whatever"}/>
                </ListItem>
            </List>
        </>
    )
}