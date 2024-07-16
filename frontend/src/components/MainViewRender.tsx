import {Site} from "../util/Interfaces.ts";
import Landingpage from "./Landingpage.tsx";
import OrderMenu from "./OrderMenu.tsx";
import Register from "./Register.tsx";
import ScheduleMenu from "./ScheduleMenu.tsx";

export default function MainViewRender({site, currentUser}: { site: Site, currentUser: string | undefined }) {
    switch (site) {
        case Site.Landingpage:
            return <Landingpage currentUser={currentUser}/>
        case Site.Menu:
            return <OrderMenu/>
        case Site.Register:
            return <Register/>
        case Site.Schedule:
            return <ScheduleMenu/>
    }
};