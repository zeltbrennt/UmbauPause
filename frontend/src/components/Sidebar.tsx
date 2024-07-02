import pauseLogo from '/pause_logo.png'
import {Site} from "../util/Interfaces.ts";

function Sidebar({setDisplay}) {
    return (
        <nav className="left">
            <a onClick={() => setDisplay(Site.Landingpage)}>
                <button className="circle transparent">
                    <img className="circle responsive" src={pauseLogo} alt="pause logo"/>
                </button>
            </a>
            <a onClick={() => setDisplay(Site.Menu)}>
                <i>menu_book</i>
                <div>Karte</div>
            </a>
            <a onClick={() => alert("TODO")}>
                <i>edit_square</i>
                <div>Edit</div>
            </a>
            <a onClick={() => alert("TODO")}>
                <i>login</i>
                <div>Login</div>
            </a>
        </nav>
    )
}

export default Sidebar