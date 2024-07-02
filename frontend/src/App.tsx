import {useEffect, useState} from 'react'
import "beercss"
import MenuItem from './MenuItem'

const api_url = "http://localhost:8080/weekly"


enum Site {
    Menu,
    Dashboard
}

export interface IMenuItem {
    name: string,
    available: boolean,
    price: number,
    scheduled: string
}

function App() {
    const [display, setDisplay] = useState(Site.Menu)
    const [items, setItems] = useState<IMenuItem[]>([])

    document.documentElement.lang = 'de'

    const getMenu = async () => {
        const response = await fetch(api_url)
        const data = await response.json()

        console.log(data.map((data: IMenuItem) => data.name))
        setItems(data)

    }

    useEffect(() => {
        getMenu()
    }, [])

    return (
        <>
            <button onClick={() => setDisplay(Site.Menu)}>display menu</button>
            <button onClick={() => setDisplay(Site.Dashboard)}>display Dashboard</button>
            <button onClick={alarm}>ALARM</button>
            {renderSwitch(display)}
            {items.map((item) => <MenuItem data={item}/>)}
        </>
    )
}

function alarm() {
    alert("WTF????")
}

function renderSwitch(site: Site) {
    switch (site) {
        case Site.Menu:
            return <Menu/>
        case Site.Dashboard:
            return <Dashboard/>
    }
}

function Menu() {
    return (
        <div>I am a Menu</div>
    )
}

function Dashboard() {
    return (
        <div>I am a Dashboard</div>
    )
}

export default App
