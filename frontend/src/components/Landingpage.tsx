import kantine_logo from '/kantine.png'

function Landingpage() {
    return (
        <div>
            <h1 className="enter-align middle-align">Hier entsteht in Zukunft eine Website von</h1>
            <div className="large-space"></div>
            <div className="center-align">
                <img src={kantine_logo} alt="kantine logo" width="500px"/>
            </div>
        </div>
    )
}

export default Landingpage