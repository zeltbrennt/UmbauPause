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
    Order,
    OrderOverview,
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

export interface DeliveryLocation {
    id: number,
    name: string,
}

export interface Order {
    item: number,
    location: number,
    orders?: number,
}

export interface OrderOverviewDta {
    validFrom: string,
    validTo: string,
    timestamp: string,
    orders: OrderCount[]
}

export interface OrderCount {
    day: number,
    location: string,
    orderCount: number,
}