export interface IWeeklyMenu {
    Montag: string,
    Dienstag: string,
    Mittwoch: string,
    Donnerstag: string,
    Freitag: string,
    validFrom: string,
    validTo: string,
}

export enum Site {
    Landingpage,
    Register,
    Menu,
    Schedule,
    Order
}

export enum MenuItemState {
    AVAILABLE,
    UNAVAILABLE,
    SELECTED
}

export interface JWTToken {
    aud: string,
    iss: string,
    iat: number,
    exp: number,
    email: string,
    role: string,
}

export enum UserRole {
    USER = "USER", MODERATOR = "MODERATOR", ADMIN = "ADMIN"
}

export interface UserPrincipal {
    email: string,
    role: UserRole
}

