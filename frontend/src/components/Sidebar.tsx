import pauseLogo from '/pause_logo.png'

function Sidebar() {
    return (
        <nav className="left">
            <a>
                <button className="circle transparent">
                    <img className="circle responsive" src={pauseLogo} alt="pause logo"/>
                </button>
            </a>
            <a>
                <i>menu_book</i>
                <div>Karte</div>
            </a>
            <a>
                <i>edit_square</i>
                <div>Edit</div>
            </a>
            <a>
                <i>login</i>
                <div>Login</div>
            </a>
        </nav>
    )
}

export default Sidebar