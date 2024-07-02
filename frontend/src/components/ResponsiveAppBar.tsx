import {AppBar, Avatar, Box, IconButton, Toolbar, Typography} from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import pauseLogo from "/pause_logo.png";

function ResponsiveAppBar() {


    return (
        <AppBar position="fixed" sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}>
            <Toolbar>
                <Box sx={{display: "flex"}}>

                    <IconButton>
                        <MenuIcon/>
                    </IconButton>
                </Box>
                <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                    (Umbau)Pause in der Kantine im DNT
                </Typography>
                <IconButton>
                    <Avatar alt="Pause Logo" src={pauseLogo}></Avatar>
                </IconButton>
            </Toolbar>
        </AppBar>
    )
}

export default ResponsiveAppBar