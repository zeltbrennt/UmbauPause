import {Container} from "@mui/material";
import AppFrame from "./components/AppFrame.tsx";
import {ThemeProvider} from "@emotion/react";
import {lightTheme} from "./Themes.ts";
import LoginDialog from "./components/LoginDialog.tsx";
import {useState} from "react";
import {Site, UserPrincipal} from "./util/Interfaces.ts";

function App() {
    const [loginDialogOpen, setLoginDialogOpen] = useState(false)
    const [mainView, setMainView] = useState(Site.Landingpage)
    const [currentUser, setCurrentUser] = useState(
        sessionStorage.getItem('userPrincipal') ?
            sessionStorage.getItem('userPrincipal') as UserPrincipal :
            null)

    return (
        <ThemeProvider theme={lightTheme}>
            <Container>
                <LoginDialog open={loginDialogOpen}
                             handleClose={() => setLoginDialogOpen(false)}
                             setCurrentUser={(user: UserPrincipal) => setCurrentUser(user)}/>
                <AppFrame currentUser={currentUser}
                          logout={() => {
                              setCurrentUser(null)
                              sessionStorage.clear()
                          }}
                          currentView={mainView}
                          changeView={(site: Site) => {
                              setMainView(site)
                          }}
                          openLoginDialog={() => setLoginDialogOpen(true)}/>
            </Container>
        </ThemeProvider>
    )
}

export default App
