import useWebSocket from "react-use-websocket";
import {useState} from "react";
import {WS_URL} from "../../util/constants.ts";

export default function LiveWebSocket() {

    const [message, setMessages] = useState<string>("")

    useWebSocket(WS_URL, {
        onOpen: () => console.log('opened'),
        onClose: () => console.log('closed'),
        onError: (error) => console.log('error', error),
        onMessage: (message) => {
            console.log('message', message)
            setMessages(message.data)
        }
    })

    return (
        <div>
            <h1>Es wurden {message ?? "0"} Essen bestellt</h1>
        </div>
    )
}