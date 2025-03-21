// You may use this file to create any models
export interface Menu{
    id: string
    name: string
    description: string
    price: number
    quantity: number
}

export interface MenuSum{
    id: string
    price: number
    quantity: number
}

export interface Order{
    username: string
    password: string
    items: MenuSum[]
}

export interface Results{
    orderId: string
    payer: string
    payee: string
    payment: number

}

export interface Combined{
    results: Results
    menu: MenuSum []
}

