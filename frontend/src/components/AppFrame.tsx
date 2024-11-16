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
import FeedbackIcon from '@mui/icons-material/Feedback';
import TableViewIcon from '@mui/icons-material/TableView';
import InsightsIcon from '@mui/icons-material/Insights';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong';
import NotesIcon from '@mui/icons-material/Notes';
import ResponsiveAppBar from "./ResponsiveAppBar.tsx";
import {UserPrincipal, UserRole} from "../util/Interfaces.ts";
import {ReactNode, useState} from "react";
import {useNavigate} from "react-router-dom";


const drawerWidth = 240

interface AppFrameProps {
    currentUser: UserPrincipal | null,
    logout: () => void,
    openLoginDialog: () => void,
    children: ReactNode
}

export default function AppFrame({
                                     currentUser,
                                     logout,
                                     openLoginDialog,
                                     children
                                 }: AppFrameProps) {


    const theme = useTheme()
    const isDesktop = useMediaQuery(theme.breakpoints.up('sm'))
    const [drawerOpen, setDrawerOpen] = useState(false)
    const navigate = useNavigate()

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
                <Box sx={{overflow: 'auto', display: 'flex', flexDirection: 'column', height: '100%'}}>
                    <List>
                        <ListItem disablePadding key="home">
                            <ListItemButton onClick={() => {
                                navigate("/")
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
                                navigate("/menu")
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
                                    navigate("/edit")
                                    setDrawerOpen(false)
                                }}>
                                    <ListItemIcon>
                                        <EditNoteIcon/>
                                    </ListItemIcon>
                                    <ListItemText primary="Karte bearbeiten"/>
                                </ListItemButton>
                            </ListItem>
                            <ListItem disablePadding key="new">
                                <ListItemButton onClick={() => {
                                    navigate("/schedule")
                                    setDrawerOpen(false)
                                }}>
                                    <ListItemIcon>
                                        <NotesIcon/>
                                    </ListItemIcon>
                                    <ListItemText primary="Neue Wochenkarte"/>
                                </ListItemButton>
                            </ListItem>
                            <ListItem disablePadding key="overview">
                                <ListItemButton onClick={() => {
                                    navigate("/statistics/this-week")
                                    setDrawerOpen(false)
                                }}>
                                    <ListItemIcon>
                                        <TableViewIcon/>
                                    </ListItemIcon>
                                    <ListItemText primary="Bestellübersicht"/>
                                </ListItemButton>
                            </ListItem>
                            <ListItem disablePadding key="tags">
                                <ListItemButton onClick={() => {
                                    navigate("/statistics/tags")
                                    setDrawerOpen(false)
                                }}>
                                    <ListItemIcon>
                                        <TableViewIcon/>
                                    </ListItemIcon>
                                    <ListItemText primary="Tags"/>
                                </ListItemButton>
                            </ListItem>
                            <ListItem disablePadding key="dashboard">
                                <ListItemButton onClick={() => {
                                    navigate("/live")
                                    setDrawerOpen(false)
                                }}>
                                    <ListItemIcon>
                                        <InsightsIcon/>
                                    </ListItemIcon>
                                    <ListItemText primary="Live update"/>
                                </ListItemButton>
                            </ListItem>
                        </> : <></>}
                        {currentUser?.roles.includes(UserRole.USER) ? <>
                            <ListItem disablePadding key={"order"}>
                                <ListItemButton onClick={() => {
                                    navigate("/neworder")
                                    setDrawerOpen(false)
                                }}>
                                    <ListItemIcon><ShoppingCartIcon/></ListItemIcon>
                                    <ListItemText primary={"Bestellen"}></ListItemText>
                                </ListItemButton>
                            </ListItem>
                            <ListItem disablePadding key="orders">
                                <ListItemButton onClick={() => {
                                    navigate("/myorders")
                                    setDrawerOpen(false)
                                }}>
                                    <ListItemIcon>
                                        <ReceiptLongIcon/>
                                    </ListItemIcon>
                                    <ListItemText primary="Meine Bestellungen"/>
                                </ListItemButton>
                            </ListItem> </> : <></>}
                        <ListItem disablePadding key="login">
                            <ListItemButton onClick={() => {
                                currentUser ? logout() : openLoginDialog()
                                setDrawerOpen(false)
                                navigate("/")
                            }}>
                                <ListItemIcon>
                                    {currentUser ? <LogoutIcon/> : <LoginIcon/>}
                                </ListItemIcon>
                                <ListItemText primary={currentUser ? "Logout" : "Login"}/>
                            </ListItemButton>
                        </ListItem>
                    </List>
                    <Box sx={{flexGrow: 1}}></Box>
                    <List>
                        <ListItem disablePadding key="feedback">
                            <ListItemButton onClick={() => {
                                setDrawerOpen(false)
                                navigate("/feedback")
                            }}>
                                <ListItemIcon>
                                    <FeedbackIcon/>
                                </ListItemIcon>
                                <ListItemText primary={"Feedback"}/>
                            </ListItemButton>
                        </ListItem>
                    </List>
                </Box>
            </SwipeableDrawer>
            <Box component="main" sx={{flexGrow: 1, p: 3}}>
                <Toolbar/>
                {children}
            </Box>
        </Box>
    )
}
