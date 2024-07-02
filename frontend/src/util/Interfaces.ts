export interface IMenuItem {
    name: string,
    available: boolean,
    price: number,
    scheduled: string
}

export enum Site {
    Landingpage,
    Menu,
}