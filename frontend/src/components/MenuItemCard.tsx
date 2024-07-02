import {IMenuItem} from "../util/Interfaces.ts";

function MenuItemCard({data}: { data?: IMenuItem }) {
    if (!data) {
        return <article className="border fill">
            <h3>Kantine geschlossen</h3>
        </article>
    }
    return (
        <article className="border fill">
            <h3>{data.scheduled}</h3>
            <h6>{data.name}</h6>
            <nav className="right-align">
                <label className="switch icon">
                    <input type="checkbox" disabled={!data.available}></input>
                    <span>
                        <i>no_meals</i>
                        <i>restaurant</i>
                    </span>
                </label>
            </nav>

        </article>
    )
}

export default MenuItemCard