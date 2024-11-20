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
import EditMenu from "./components/manageContent/EditMenu.tsx";
import MakeOrder from "./components/ordering/MakeOrder.tsx";
import OrderOverview from "./components/dashboard/OrderOverview.tsx";
import LiveWebSocket from "./components/dashboard/LiveWebSocket.tsx";
import Landingpage from "./components/Landingpage.tsx";
import ShowCurrentMenu from "./components/ordering/ShowCurrentMenu.tsx";
import ValidateEmail from "./components/userAdministration/ValidateEmail.tsx";
import Feedback from "./components/Feedback.tsx";
import {Container} from "@mui/material";
import MyOrders from "./components/ordering/MyOrders.tsx";
import TagStatistics from "./components/dashboard/TagStatistics.tsx";
import PaymentSuccess from "./components/ordering/PaymentSuccess.tsx";
import PaymentCancel from "./components/ordering/PaymentCancel.tsx";

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
                <Container disableGutters>
                    <LoginDialog open={loginDialogOpen}
                                 handleClose={() => setLoginDialogOpen(false)}
                                 setCurrentUser={(user: UserPrincipal) => setCurrentUser(user)}
                                 handleRegister={() => setLoginDialogOpen(false)}
                    />
                    <AppFrame currentUser={currentUser}
                              logout={() => {
                                  sessionStorage.clear()
                                  setCurrentUser(null)
                              }}
                              openLoginDialog={() => setLoginDialogOpen(true)}>
                        <Routes>
                            <Route path="/" element={<Landingpage/>}/>
                            <Route path="/menu" element={<ShowCurrentMenu/>}/>
                            <Route path="/edit" element={<EditMenu/>}/>
                            <Route path="/register" element={<Register/>}/>
                            <Route path="/schedule" element={<ScheduleMenu/>}/>
                            <Route path="/neworder" element={<MakeOrder/>}/>
                            <Route path="/myorders" element={<MyOrders/>}/>
                            <Route path="/statistics/this-week" element={<OrderOverview/>}/>
                            <Route path="/statistics/tags" element={<TagStatistics/>}/>
                            <Route path="/live" element={<LiveWebSocket/>}/>
                            <Route path="/user/verify" element={<ValidateEmail/>}/>
                            <Route path="/feedback" element={<Feedback/>}/>
                            <Route path="/success" element={<PaymentSuccess/>}/>
                            <Route path="/cancel" element={<PaymentCancel/>}/>
                        </Routes>
                    </AppFrame>
                </Container>
            </ThemeProvider>
        </BrowserRouter>
    )
}

export default App
