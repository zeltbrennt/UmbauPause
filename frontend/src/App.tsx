import {Container} from "@mui/material";
import ClippedDrawer from "./components/ClippedDrawer.tsx";
import {ThemeProvider} from "@emotion/react";
import {lightTheme} from "./Themes.ts";
import LoginDialog from "./components/LoginDialog.tsx";
import {useState} from "react";
import {Site} from "./util/Interfaces.ts";

function App() {
    const [loginDialogOpen, setLoginDialogOpen] = useState(false)
    const [mainView, setMainView] = useState(Site.Landingpage)
    return (
        <ThemeProvider theme={lightTheme}>
            <Container>
                <LoginDialog open={loginDialogOpen} handleClose={() => setLoginDialogOpen(false)}/>
                <ClippedDrawer currentView={mainView} changeView={(site: Site) => {setMainView(site)}} openLoginDialog={() => setLoginDialogOpen(true)}/>
            </Container>
        </ThemeProvider>
    )
}

export default App
