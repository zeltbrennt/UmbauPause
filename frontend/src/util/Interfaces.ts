export interface MenuInfo {
    validFrom: string,
    validTo: string,
    dishes: MenuItem[]
}

export interface MenuItem {
    id: number,
    name: string,
    day: string,
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
    userId: string,
    role: string,
}

export enum UserRole {
    USER = "USER", MODERATOR = "MODERATOR", ADMIN = "ADMIN"
}

export interface UserPrincipal {
    userId: string,
    role: UserRole
}

