import { defineStore } from 'pinia'
import type { Product } from '~/types/product'

export const useProductStore = defineStore('productStore', {
  state: () => ({
    products: [] as Product[],
    product: {} as Product,
    isLoading: false,
    error: null as string | null
  }),

  getters: {
    getProductById: (state) => {
      return (id: number) => state.products.find(p => p.id === id)
    }
  },

  actions: {
    async getProducts() {
      this.isLoading = true
      this.error = null
      try {
        const data = await $fetch<Product[]>('http://localhost:4000/products')
        this.products = data
        return data
      } catch (err) {
        this.error = 'Failed to fetch products. Please make sure json-server is running on port 4000'
        console.error('Error fetching products:', err)
        return null
      } finally {
        this.isLoading = false
      }
    },

    async getProduct(productId: number) {
      // First try to get from existing products
      const existingProduct = this.getProductById(productId)
      if (existingProduct) {
        this.product = existingProduct
        return existingProduct
      }

      this.isLoading = true
      this.error = null
      try {
        const data = await $fetch<Product>(`http://localhost:4000/products/${productId}`)
        if (!data) {
          throw new Error('Product not found')
        }
        this.product = data
        return data
      } catch (err) {
        if (err instanceof Error && err.message === 'Product not found') {
          this.error = 'Product not found'
        } else {
          this.error = 'Failed to fetch product. Please make sure json-server is running on port 4000'
        }
        console.error('Error fetching product:', err)
        return null
      } finally {
        this.isLoading = false
      }
    }
  }
})
