import {Container} from "@mui/material";
import AppFrame from "./components/AppFrame.tsx";
import {ThemeProvider} from "@emotion/react";
import {lightTheme} from "./Themes.ts";
import LoginDialog from "./components/userAdministration/LoginDialog.tsx";
import {useState} from "react";
import {JWTToken, UserPrincipal} from "./util/Interfaces.ts";
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import {getUrlFrom} from "./util/functions.ts";
import Register from "./components/userAdministration/Register.tsx";
import ScheduleMenu from "./components/manageContent/ScheduleMenu.tsx";
import MakeOrder from "./components/ordering/MakeOrder.tsx";
import OrderOverview from "./components/dashboard/OrderOverview.tsx";
import LiveWebSocket from "./components/dashboard/LiveWebSocket.tsx";
import Landingpage from "./components/Landingpage.tsx";
import ShowCurrentMenu from "./components/ordering/ShowCurrentMenu.tsx";

function App() {

    const [loginDialogOpen, setLoginDialogOpen] = useState(false)
    const [currentUser, setCurrentUser] = useState(
        sessionStorage.getItem('userPrincipal') ?
            JSON.parse(sessionStorage?.getItem('userPrincipal') || '') as UserPrincipal :
            null)

    const serverLogout = async (accessToken: JWTToken) => {
        const logoutUrl = getUrlFrom("user", "logout");
        const response = await fetch(logoutUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            }
        })
        if (response.status === 200) {
            setCurrentUser(null)
            sessionStorage.clear()
        }
    }

    return (
        <BrowserRouter>
            <ThemeProvider theme={lightTheme}>
                <Container>
                    <LoginDialog open={loginDialogOpen}
                                 handleClose={() => setLoginDialogOpen(false)}
                                 setCurrentUser={(user: UserPrincipal) => setCurrentUser(user)}
                                 handleRegister={() => setLoginDialogOpen(false)}
                    />
                    <AppFrame currentUser={currentUser}
                              logout={() => {
                                  serverLogout(sessionStorage.getItem("accessToken") as unknown as JWTToken)
                              }}
                              openLoginDialog={() => setLoginDialogOpen(true)}>
                        <Routes>
                            <Route path="/" element={<Landingpage/>}/>
                            <Route path="/menu" element={<ShowCurrentMenu/>}/>
                            <Route path="/register" element={<Register/>}/>
                            <Route path="/schedule" element={<ScheduleMenu/>}/>
                            <Route path="/order" element={<MakeOrder/>}/>
                            <Route path="/statistics/this-week" element={<OrderOverview/>}/>
                            <Route path="/live" element={<LiveWebSocket/>}/>
                        </Routes>
                    </AppFrame>
                </Container>
            </ThemeProvider>
        </BrowserRouter>
    )
}

export default App
