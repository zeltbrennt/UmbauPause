import {Button, List, ListItem, Typography} from "@mui/material";
import MenuItemCard from "./MenuItemCard.tsx";
import {useState} from "react";

export default function MakeOrder() {

    const [monday, setMonday] = useState(false);
    const [tuesday, setTuesday] = useState(false);
    const [wednesday, setWednesday] = useState(false);
    const [thursday, setThursday] = useState(false);
    const [friday, setFriday] = useState(false);
    const handleClick = () => {
        console.log({
            Montag: monday,
            Dienstag: tuesday,
            Mittwoch: wednesday,
            Donnerstag: thursday,
            Freitag: friday,
        })
    }
    return (
        <>
            <Typography variant={"h3"} textAlign={"center"}>Bestellen</Typography>
            <Typography textAlign={"center"}>vom ??? bis ??? </Typography>
            <List>
                <ListItem>
                    <MenuItemCard day={"Montag"} dish={"whatever"} handleClick={() => setMonday(true)}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Dienstag"} dish={"whatever"} handleClick={() => setTuesday(!tuesday)}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Mittwoch"} dish={"whatever"} handleClick={() => setWednesday(!wednesday)}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Donnerstag"} dish={"whatever"} handleClick={() => setThursday(!thursday)}/>
                </ListItem>
                <ListItem>
                    <MenuItemCard day={"Freitag"} dish={"whatever"} handleClick={() => setFriday(!friday)}/>
                </ListItem>
            </List>
            <Button variant={"contained"} onClick={handleClick}>Bestellen</Button>
        </>
    )
}

