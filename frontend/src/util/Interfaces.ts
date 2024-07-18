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
    Schedule
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

export interface Dish {
    description: string,
    available: boolean,
    scheduled: string,
    price: number
}