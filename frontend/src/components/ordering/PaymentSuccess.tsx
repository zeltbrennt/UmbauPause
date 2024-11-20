import {useEffect} from "react";
import {getUrlFrom} from "../../util/functions.ts";

export default function PaymentSuccess() {


    useEffect(() => {

        const payload = atob(window.location.search.replace("?orders=", ""))
        console.log(payload)
        const saveOrders = async () => {
            const orderUrl = getUrlFrom("order")
            console.log(orderUrl)
            fetch(orderUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
                },
                body: payload
            })
        }
        saveOrders().catch((reason) => console.log(`could not save orders to database: ${reason}`))
    }, []);

    return (
        <div>
            <h1>Payment successful</h1>
        </div>
    )
}