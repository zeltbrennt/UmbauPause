import {BASE_URL} from "./constants.ts";

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


export const formatNumberToCurrency = (value: number) => {
    return new Intl.NumberFormat('de-DE', {minimumFractionDigits: 2, maximumFractionDigits: 2}).format(value / 100)
}

export const parseNumber = (formattedValue: string) => {
    const numericValue = formattedValue.replace(/[^0-9,-]+/g, '').replace(',', '.');
    return numericValue ? parseFloat(numericValue) : 0;
};