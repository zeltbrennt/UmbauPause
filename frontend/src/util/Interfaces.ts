export interface MenuInfo {
    validFrom: string,
    validTo: string,
    dishes: MenuItem[]
}

export interface MenuItem {
    id: number,
    name: string,
    day: number,
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
    uid: string,
    roles: string[],
}

export enum UserRole {
    ADMIN = "ADMIN", USER = "USER",
}

export interface UserPrincipal {
    uid: string,
    roles: UserRole[]
}

