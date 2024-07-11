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
import LogoutIcon from '@mui/icons-material/Logout';
import HomeIcon from '@mui/icons-material/Home';
import TableViewIcon from '@mui/icons-material/TableView';
import InsightsIcon from '@mui/icons-material/Insights';
import ResponsiveAppBar from "./ResponsiveAppBar.tsx";
import {Site} from "../util/Interfaces.ts";
import MainViewRender from './MainViewRender.tsx'
import {useState} from "react";


const drawerWidth = 240

interface ClippedDrawerProps {
    currentUser: string,
    logout: () => void,
    currentView: Site,
    changeView: (site: Site) => void,
    openLoginDialog: () => void
}

export default function ClippedDrawer({
                                          currentUser,
                                          logout,
                                          currentView,
                                          changeView,
                                          openLoginDialog
                                      }: ClippedDrawerProps) {


    const theme = useTheme()
    const isDesktop = useMediaQuery(theme.breakpoints.up('sm'))
    const [drawerOpen, setDrawerOpen] = useState(false)


    return (
        <Box sx={{display: 'flex'}}>

            <ResponsiveAppBar drawerState={drawerOpen} setDrawerState={setDrawerOpen}/>
            <SwipeableDrawer variant={isDesktop ? 'permanent' : 'temporary'}
                             open={isDesktop ? true : drawerOpen}
                             onClose={() => setDrawerOpen(false)}
                             onOpen={() => setDrawerOpen(true)}
                             sx={{
                                 width: drawerWidth,
                                 flexShrink: 0,
                             }}>
                <Toolbar/>
                <Box sx={{overflow: 'auto'}}>
                    <List>
                        <ListItem disablePadding key="home">
                            <ListItemButton onClick={() => {
                                changeView(Site.Landingpage)
                                setDrawerOpen(false)
                            }}>
                                <ListItemIcon>
                                    <HomeIcon/>
                                </ListItemIcon>
                                <ListItemText primary="Home"/>
                            </ListItemButton>
                        </ListItem>
                        <ListItem disablePadding key="menu">
                            <ListItemButton onClick={() => {
                                changeView(Site.Menu)
                                setDrawerOpen(false)
                            }
                            }>
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
                        <ListItem disablePadding key="overview">
                            <ListItemButton onClick={() => alert("not implemented")}>
                                <ListItemIcon>
                                    <TableViewIcon/>
                                </ListItemIcon>
                                <ListItemText primary="Bestellübersicht"/>
                            </ListItemButton>
                        </ListItem>
                        <ListItem disablePadding key="dashboard">
                            <ListItemButton onClick={() => alert("not implemented")}>
                                <ListItemIcon>
                                    <InsightsIcon/>
                                </ListItemIcon>
                                <ListItemText primary="Dashboard"/>
                            </ListItemButton>
                        </ListItem>
                        <ListItem disablePadding key="login">
                            <ListItemButton onClick={currentUser === "" ? openLoginDialog : logout}>
                                <ListItemIcon>
                                    {currentUser === "" ? <LoginIcon/> : <LogoutIcon/>}
                                </ListItemIcon>
                                <ListItemText primary={currentUser === "" ? "Login" : "Logout"}/>
                            </ListItemButton>
                        </ListItem>
                    </List>
                </Box>
            </SwipeableDrawer>
            <Box component="main" sx={{flexGrow: 1, p: 3}}>
                <Toolbar/>
                <MainViewRender site={currentView}/>
            </Box>
        </Box>
    )
}
