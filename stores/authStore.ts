import { defineStore } from 'pinia'
import type { User } from '~/types/user'

export const useAuthStore = defineStore('auth', {
  state: () => {
    return {
      user: null as User | null
    }
  },

  getters: {
    isAuthenticated: () => {
      if (typeof window !== 'undefined') {
        return !!localStorage.getItem('authToken');
      }
      return false;
    },
    getUser: (state) => state.user
  },

  actions: {
    async register(registrationData: any) {
      try {
        const response = await $fetch('http://localhost:5001/auth/register', {
          method: 'POST',
          body: registrationData,
        })
        return response
      } catch (error) {
        throw new Error(`Registration failed. Please try again later. Error: ${error}`)
      }
    },

    async login(loginData: any) {
      try {
        const response = await $fetch<string>('http://localhost:5001/auth/login', {
          method: 'POST',
          body: loginData,
        })

        if (!response) {
          throw new Error('Invalid response from server')
        }
        
        if (typeof window !== 'undefined') {
          localStorage.setItem('authToken', response)
        }

        // Fetch user details
        const user = await $fetch<User>(`http://localhost:5001/user/${loginData.email}`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${response}`,
          },
        })
        
        if (user) {
          this.user = user
          return true
        } else {
          throw new Error('User not found.')
        }
      } catch (error) {
        console.error('Login error:', error)
        throw error
      }
    },

    logout() {
      console.log('Logging out...')
      this.user = null
      if (typeof window !== 'undefined') {
        localStorage.removeItem('authToken')
      }
      navigateTo('/login')
    },

    // Initialize auth state from localStorage
    initAuth() {
      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('authToken')
        if (token) {
          this.fetchUser()
        }
      }
    },

    // Fetch user details using stored token
    async fetchUser() {
      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('authToken')
        if (!token) return
        console.log('Fetching user...')
        console.log('Fetching user with token:', token)
        try {
          const user = await $fetch<User>(`http://localhost:5001/user/${this.user?.email}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          this.user = user
          console.log('User fetched:', user)
        } catch (error) {
          this.logout()
        }
      }
    }
  }
})
