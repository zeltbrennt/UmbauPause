import {getUrlFrom} from "../../util/functions.ts";
import {useEffect, useState} from "react";
import {Typography} from "@mui/material";

export default function PriceDisplay() {

    const [price, setPrice] = useState(0)
    const getPrice = async () => {
        let priceUrl = getUrlFrom("default-price")
        let response = await fetch(priceUrl)
        let value = await response.text()
        setPrice(value as number / 100)
    }

    useEffect(() => {
        getPrice().catch(reason => console.log(`couldn't fetch price: ${reason}`))
    }, []);

    return (
        <Typography>Preis je Gericht: {new Intl.NumberFormat('de-DE', {
            style: 'currency',
            currency: 'EUR'
        }).format(price)}</Typography>
    )
}