import {
    Box,
    List,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    SwipeableDrawer,
    Toolbar,
    useMediaQuery,
    useTheme
} from "@mui/material";
import MenuBookIcon from '@mui/icons-material/MenuBook';
import EditNoteIcon from '@mui/icons-material/EditNote';
import LoginIcon from '@mui/icons-material/Login';
import HomeIcon from '@mui/icons-material/Home';
import ResponsiveAppBar from "./ResponsiveAppBar.tsx";
import {Site} from "../util/Interfaces.ts";
import Landingpage from "./Landingpage.tsx";
import OrderMenu from "./OrderMenu.tsx";
import {useState} from "react";


const drawerWidth = 240

function renderSwitch(site: Site) {
    switch (site) {
        case Site.Landingpage:
            return <Landingpage/>
        case Site.Menu:
            return <OrderMenu/>
    }
}

export default function ClippedDrawer() {

    const [display, setDisplay] = useState(Site.Landingpage)
    const theme = useTheme()
    const isDesktop = useMediaQuery(theme.breakpoints.up('sm'))
    const [open, setOpen] = useState(false)

    return (
        <Box sx={{display: 'flex'}}>
            <ResponsiveAppBar drawerState={open} setDrawerState={setOpen}/>
            <SwipeableDrawer variant={isDesktop ? 'permanent' : 'temporary'}
                             open={isDesktop ? true : open}
                             onClose={() => setOpen(false)}
                             onOpen={() => setOpen(true)}
                             sx={{
                                 width: drawerWidth,
                                 flexShrink: 0,
                             }}>
                <Toolbar/>
                <Box sx={{overflow: 'auto'}}>
                    <List>
                        <ListItem disablePadding key="home">
                            <ListItemButton onClick={() => setDisplay(Site.Landingpage)}>
                                <ListItemIcon>
                                    <HomeIcon/>
                                </ListItemIcon>
                                <ListItemText primary="Home"/>
                            </ListItemButton>
                        </ListItem>
                        <ListItem disablePadding key="menu">
                            <ListItemButton onClick={() => setDisplay(Site.Menu)}>
                                <ListItemIcon>
                                    <MenuBookIcon/>
                                </ListItemIcon>
                                <ListItemText primary="Wochenkarte"/>
                            </ListItemButton>
                        </ListItem>
                        <ListItem disablePadding key="edit">
                            <ListItemButton onClick={() => alert("not implemented")}>
                                <ListItemIcon>
                                    <EditNoteIcon/>
                                </ListItemIcon>
                                <ListItemText primary="Karte bearbeiten"/>
                            </ListItemButton>
                        </ListItem>
                        <ListItem disablePadding key="login">
                            <ListItemButton onClick={() => alert("not implemented")}>
                                <ListItemIcon>
                                    <LoginIcon/>
                                </ListItemIcon>
                                <ListItemText primary="Login"/>
                            </ListItemButton>
                        </ListItem>
                    </List>
                </Box>
            </SwipeableDrawer>
            <Box component="main" sx={{flexGrow: 1, p: 3}}>
                <Toolbar/>
                {renderSwitch(display)}
            </Box>
        </Box>
    )
}
