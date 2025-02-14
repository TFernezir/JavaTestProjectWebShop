import { defineStore } from 'pinia'
import type { CartItem } from '~/types/cart'

export const useCartStore = defineStore('cart', {
  state: () => ({
    cart: [] as CartItem[]
   }),
  actions: {
    async getCart() {
      const data =  await $fetch<CartItem[]>('http://localhost:4000/cart')
      this.cart = data
      console.log(this.cart)
    }
  }
})
