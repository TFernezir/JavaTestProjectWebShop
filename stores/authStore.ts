import { defineStore } from 'pinia'
import type { User } from '@/types/user'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as User | null,
    token: null as String | null
   }),

  actions: {
    async register(registrationData: any) {
      try {
        const response = await $fetch('http://localhost:5001/auth/register', {
          method: 'POST',
          body: registrationData,
        });
      } catch (error) {
        throw new Error('Registration failed. Please try again later.' + "Error: " + error);
      }
    },

    async login(loginData: any){
      try {
        const response = await $fetch('http://localhost:5001/auth/login', {
          method: 'POST',
          body: loginData,
        });

        console.log('Login Response: ', response);

        // this.token = response;

        if (!response) {
          throw new Error('Invalid response from server. Missing email.');
        }

        // const user = await $fetch(`http://localhost:5001/user/${loginData.email}`, {
        //   method: 'GET',
        //   headers: {
        //     Authorization: `Bearer ${response}`,
        //   },
        // });
        
        // console.log('User: '+ user)
        // if (user) {
        //   this.user = user;
        // } else {
        //   throw new Error('User not found.');
        // }
      } catch (error) {
        
        console.log("Error: " + error)
        throw error;
      }
    }
  }
})
