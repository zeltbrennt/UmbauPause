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
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import ResponsiveAppBar from "./ResponsiveAppBar.tsx";
import {Site, UserPrincipal, UserRole} from "../util/Interfaces.ts";
import MainViewRender from './MainViewRender.tsx'
import {useState} from "react";


const drawerWidth = 240

interface AppFrameProps {
    currentUser: UserPrincipal | null,
    logout: () => void,
    currentView: Site,
    changeView: (site: Site) => void,
    openLoginDialog: () => void
}

export default function AppFrame({
                                     currentUser,
                                     logout,
                                     currentView,
                                     changeView,
                                     openLoginDialog
                                 }: AppFrameProps) {


    const theme = useTheme()
    const isDesktop = useMediaQuery(theme.breakpoints.up('sm'))
    const [drawerOpen, setDrawerOpen] = useState(false)
    return (
        <Box sx={{display: 'flex'}}>

            <ResponsiveAppBar drawerState={drawerOpen} setDrawerState={setDrawerOpen} isDesktop={isDesktop}/>
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
                        {currentUser?.roles.includes(UserRole.ADMIN) ? <>
                            <ListItem disablePadding key="edit">
                                <ListItemButton onClick={() => {
                                    changeView(Site.Schedule)
                                    setDrawerOpen(false)
                                }}>
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
                        </> : <></>}
                        {currentUser?.roles.includes(UserRole.USER) ?
                            <ListItem disablePadding key={"order"}>
                                <ListItemButton onClick={() => changeView(Site.Order)}>
                                    <ListItemIcon><ShoppingCartIcon/></ListItemIcon>
                                    <ListItemText primary={"Bestellen"}></ListItemText>
                                </ListItemButton>
                            </ListItem> : <></>}
                        <ListItem disablePadding key="login">
                            <ListItemButton onClick={() => {
                                currentUser ? logout() : openLoginDialog()
                                setDrawerOpen(false)
                            }}>
                                <ListItemIcon>
                                    {currentUser ? <LogoutIcon/> : <LoginIcon/>}
                                </ListItemIcon>
                                <ListItemText primary={currentUser ? "Logout" : "Login"}/>
                            </ListItemButton>
                        </ListItem>
                    </List>
                </Box>
            </SwipeableDrawer>
            <Box component="main" sx={{flexGrow: 1, p: 3}}>
                <Toolbar/>
                <MainViewRender site={currentView} currentUser={currentUser?.uid}/>
            </Box>
        </Box>
    )
}
