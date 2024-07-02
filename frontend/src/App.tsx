import {useState} from 'react'
import "beercss"
import {Site} from "./util/Interfaces.ts";
import Landingpage from "./components/Landingpage.tsx";
import OrderMenu from "./components/OrderMenu.tsx";
import Sidebar from "./components/Sidebar.tsx";
import kantine_logo from "/kantine.png";


function App() {
    const [display, setDisplay] = useState(Site.Landingpage)

    document.documentElement.lang = 'de'

    return (
        <div>
            <Sidebar setDisplay={setDisplay}/>
            <main className="responsive">
                <article className="padding center-align">
                    <img src={kantine_logo} alt="kantine logo" width="400px"/>
                </article>
                <div className="large-space"></div>
                {renderSwitch(display)}
            </main>
        </div>
    )
}

function renderSwitch(site: Site) {
    switch (site) {
        case Site.Landingpage:
            return <Landingpage/>
        case Site.Menu:
            return <OrderMenu/>
    }
}


export default App
