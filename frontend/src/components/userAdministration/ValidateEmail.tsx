import {useSearchParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {BASE_URL} from "../../util/constants.ts";

export default function ValidateEmail() {

    const [searchParams] = useSearchParams()
    const userId = searchParams.get('id')
    const [success, setSuccess] = useState(false)

    async function verify() {
        const verifyUrl = `${BASE_URL}/user/verify?id=${userId}`
        const response = await fetch(verifyUrl, {
            method: 'POST'
        })
        if (response.status === 200) {
            setSuccess(true)
        }
    }

    useEffect(() => {
        verify().catch(e => console.error(e))
    }, [])

    return (
        <div>
            <h1>Verify User</h1>
            {success ? (
                <p>Erfolgreich validiert</p>
            ) : (
                <p>No User ID provided</p>
            )}
        </div>
    )

}