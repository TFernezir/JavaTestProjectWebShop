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
          throw new Error('INVALID_RESPONSE')
        }
        
        if (typeof window !== 'undefined') {
          localStorage.setItem('authToken', response)
        }

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
          throw new Error('USER_NOT_FOUND')
        }
      } catch (error: any) {
        console.error('Login error:', error)
        
        // Handle specific error cases
        if (error.response?.status === 401) {
          throw new Error('Invalid email or password')
        } else if (error.message === 'USER_NOT_FOUND') {
          throw new Error('User not found')
        } else if (error.message === 'INVALID_RESPONSE') {
          throw new Error('Server error. Please try again later')
        } else if (!navigator.onLine) {
          throw new Error('No internet connection. Please check your network')
        } else {
          throw new Error('An error occurred during login. Please try again')
        }
      }
    },

    logout() {
      this.user = null
      if (typeof window !== 'undefined') {
        localStorage.removeItem('authToken')
      }
      navigateTo('/login')
    },

    async initAuth() {
      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('authToken')
        if (token && !this.user) {
          return await this.fetchUser()
        }
        return this.user
      }
    },

    async fetchUser() {
      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('authToken')
        if (!token) return null
        
        try {
          const tokenData = JSON.parse(atob(token.split('.')[1]))
          const userEmail = tokenData.sub
          
          if (!userEmail) return null

          const user = await $fetch<User>(`http://localhost:5001/user/${userEmail}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          this.user = user
          return user
        } catch (error) {
          this.logout()
          return null
        }
      }
      return null
    }
  }
})
