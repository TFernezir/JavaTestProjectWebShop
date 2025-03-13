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
        const data = await $fetch<Product[]>('http://localhost:5001/products', {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${localStorage.getItem('authToken')}`,
          },
        })
        this.products = data
        return data
      } catch (err) {
        this.error = 'Failed to fetch products.'
        console.error('Error fetching products:', err)
        return null
      } finally {
        this.isLoading = false
      }
    },

    async getProduct(productId: number) {
      const existingProduct = this.getProductById(productId)
      if (existingProduct) {
        this.product = existingProduct
        return existingProduct
      }

      this.isLoading = true
      this.error = null
      try {
        const data = await $fetch<Product>(`http://localhost:5001/products/${productId}`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${localStorage.getItem('authToken')}`,
          },
        })
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
    },

    async addProduct(product: Product, image: File) {
      this.isLoading = true;
      this.error = null;
      console.log('Starting addProduct with:', product, image);
      
      // Create FormData object
      const formData = new FormData();
      
      // Add the product as a simple string parameter - exactly as we would in Postman
      formData.append("product", JSON.stringify(product));
      
      // Add the image file
      formData.append("image", image);
      
      try {
        console.log('FormData contents:', [...formData.entries()].map(e => `${e[0]}: ${typeof e[1] === 'string' ? e[1] : '[File]'}`));
        
        // Don't set any headers - let the browser handle it automatically
        // This matches how Postman would send the request
        const response = await fetch('http://localhost:5001/products/product', {
          method: 'POST',
          body: formData,
          headers: {
            Authorization: `Bearer ${localStorage.getItem('authToken')}`,
          },
        });

        if (!response.ok) {
          const errorText = await response.text();
          console.error('Server response:', errorText);
          throw new Error(`Failed to add product: ${response.status} ${response.statusText}`);
        }

        const savedProduct = await response.json();
        console.log('Product added successfully:', savedProduct);
        this.products.push(savedProduct);
        return savedProduct;
      } catch (error) {
        this.error = 'Failed to add product.';
        console.error('Error adding product:', error);
        throw error;
      } finally {
        this.isLoading = false;
      }
    }
  }
})
