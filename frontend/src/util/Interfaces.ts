export interface IMenuItem {
    description: string,
    available: boolean,
    price: number,
    scheduled: string
}

export enum Site {
    Landingpage,
    Menu,
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