import {Site} from "../util/Interfaces.ts";
import Landingpage from "./Landingpage.tsx";
import OrderMenu from "./OrderMenu.tsx";

export default function MainViewRender({site}: {site: Site}) {
    switch (site) {
        case Site.Landingpage:
            return <Landingpage/>
        case Site.Menu:
            return <OrderMenu/>
    }
};