export interface IWeeklyMenu {
    monday: string,
    tuesday: string,
    wednesday: string,
    thursday: string,
    friday: string,
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