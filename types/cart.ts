export interface CartItem {
  id: number;
  quantity: number;
  unitPrice: number;
  product: {
    id: number;
    name: string;
    price: number;
    about: string;
    img: string;
  }
}

export interface Basket {
  id: number;
  userId: number;
  status: string;
  items: CartItem[];
  totalAmount: number;
}