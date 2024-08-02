import {Site} from "../util/Interfaces.ts";
import Landingpage from "./Landingpage.tsx";
import ShowCurrentMenu from "./ShowCurrentMenu.tsx";
import Register from "./Register.tsx";
import ScheduleMenu from "./ScheduleMenu.tsx";
import MakeOrder from "./MakeOrder.tsx";
import OrderOverview from "./OderOverview.tsx";

export default function MainViewRender({site, currentUser}: { site: Site, currentUser: string | undefined }) {
    switch (site) {
        case Site.Landingpage:
            return <Landingpage currentUser={currentUser}/>
        case Site.Menu:
            return <ShowCurrentMenu/>
        case Site.Register:
            return <Register/>
        case Site.Schedule:
            return <ScheduleMenu/>
        case Site.Order:
            return <MakeOrder/>
        case Site.OrderOverview:
            return <OrderOverview/>
    }
};