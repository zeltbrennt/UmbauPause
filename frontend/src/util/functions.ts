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