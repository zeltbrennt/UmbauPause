import {BASE_URL} from "./constants.ts";
import {jwtDecode} from "jwt-decode";
import {JWTToken} from "./Interfaces.ts";

export function mapDayOfWeek(day: number): string {
    switch (day) {
        case 1:
            return "Montag"
        case 2:
            return "Dienstag"
        case 3:
            return "Mittwoch"
        case 4:
            return "Donnerstag"
        case 5:
            return "Freitag"
        case 6:
            return "Samstag"
        case 7:
            return "Sonntag"
        default:
            return "Ungültig"
    }
}

export function getUrlFrom(...path: string[]): string {
    return BASE_URL + path.join("/")
}

export function isAccessTokenValid() {
    if (!sessionStorage.getItem("accessToken")) {
        console.log("has no token")
        return false
    } else {
        console.log("has token")
        const decodedToken = jwtDecode<JWTToken>(sessionStorage.getItem("accessToken"))
        return decodedToken.exp * 1000 > Date.now()
    }
}

export function saveNewAccessToken() {
    fetch(getUrlFrom("refresh"), {
        method: 'POST',
        headers: {
            "Content-Type": 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("refreshToken")}`
        }
    }).then(response => {
        if (response.ok) {
            response.json().then(data => {
                sessionStorage.setItem("accessToken", data.accessToken)
                console.log("Token refreshed")
            })
        } else {
            console.log("Token refresh failed")
            sessionStorage.clear()
            location.href = "/"
        }
    })
}

export async function withTokenRefresh(fetchFunction: () => Promise<Response>): Promise<Response> {
    if (!isAccessTokenValid()) {
        await saveNewAccessToken()
    }
    return fetchFunction()
}