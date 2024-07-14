import {Site} from "../util/Interfaces.ts";
import Landingpage from "./Landingpage.tsx";
import OrderMenu from "./OrderMenu.tsx";

export default function MainViewRender({site, currentUser}: { site: Site, currentUser: string | undefined }) {
    switch (site) {
        case Site.Landingpage:
            return <Landingpage currentUser={currentUser}/>
        case Site.Menu:
            return <OrderMenu/>
    }
};