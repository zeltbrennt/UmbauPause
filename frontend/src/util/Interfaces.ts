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