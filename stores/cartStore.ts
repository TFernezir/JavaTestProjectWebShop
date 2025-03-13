import { defineStore } from 'pinia'
import type { Basket } from '~/types/cart'

export const useCartStore = defineStore('cart', {
  state: () => ({
    basket: null as Basket | null,
    error: null as string | null,
    isLoading: false,
    loadingProductIds: new Set<number>()
  }),

  getters: {
    cartItems: (state) => state.basket?.items || [],
    totalAmount: (state) => state.basket?.totalAmount || 0,
    isProductLoading: (state) => (productId: number) => state.loadingProductIds.has(productId)
  },

  actions: {
    async getCart() {
      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('authToken')
        const authStore = useAuthStore()

        this.isLoading = true
        this.error = null

        try {
          if (!token) {
            this.error = 'Please log in to view your cart'
            return null
          }

          if (!authStore.user) {
            await authStore.initAuth()
          }

          if (!authStore.user?.id) {
            this.error = 'Unable to load user data'
            return null
          }

          const data = await $fetch<Basket>(`http://localhost:5001/basket/user/${authStore.user.id}`, {
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          this.basket = data
          return data
        } catch (error) {
          this.error = 'Failed to load cart'
          return null
        } finally {
          this.isLoading = false
        }
      }
      return null
    },

    async addToCart(productId: number, quantity: number = 1) {
      const authStore = useAuthStore()
      const token = localStorage.getItem('authToken')

      if (!token) {
        this.error = 'Please log in to add items to your cart'
        return
      }

      if (!authStore.user?.id) {
        this.error = 'User not found'
        return
      }

      this.error = null
      this.loadingProductIds.add(productId)

      try {
        const updatedBasket = await $fetch<Basket>('http://localhost:5001/basket/items', {
          method: 'POST',
          query: {
            basketId: this.basket?.id,
            userId: authStore.user.id
          },
          body: {
            productId,
            quantity
          },
          headers: {
            Authorization: `Bearer ${token}`
          }
        })

        this.basket = updatedBasket
      } catch (error: any) {
        this.error = error.message || 'Failed to add item to cart'
      } finally {
        this.loadingProductIds.delete(productId)
      }
    },

    async removeFromCart(itemId: number) {
      if (!this.basket?.id) {
        this.error = 'No active basket found'
        return
      }

      const token = localStorage.getItem('authToken')
      if (!token) {
        this.error = 'Please log in to remove items from your cart'
        return
      }

      this.error = null
      this.loadingProductIds.add(itemId)

      try {
        const updatedBasket = await $fetch<Basket>(`http://localhost:5001/basket/${this.basket.id}/items/${itemId}`, {
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`
          }
        })

        this.basket = updatedBasket
      } catch (error: any) {
        this.error = error.message || 'Failed to remove item from cart'
      } finally {
        this.loadingProductIds.delete(itemId)
      }
    }
  }
})
