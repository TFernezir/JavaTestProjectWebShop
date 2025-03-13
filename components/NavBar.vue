<template>
  <div
    class="flex items-center justify-between p-2"
  >
    <!-- Header logo -->
    <NuxtLink to="/">
      <img class="mx-auto h-10 w-auto" src="/images/logo.png" alt="Logo" />
    </NuxtLink>

    <!-- Mobile toggle -->
    <div class="md:hidden">
      <button @click="toggleDrawer">
        <span class="material-symbols-outlined"> menu </span>
      </button>
    </div>

    <!-- Navbar -->
     <div v-if="!authStore.isAuthenticated">
      <NuxtLink
        to="/login"
        class="cta bg-blue-500 hover:bg-blue-600 px-3 py-2 rounded text-white font-semibold"
        >Sign Up
      </NuxtLink>
    </div>
    <div v-if="authStore.isAuthenticated" class="hidden md:block">
      <ul class="flex space-x-8 text-sm font-sans">
        <li>
          <NuxtLink 
            to="/" 
            :class="[$route.path === '/' ? 'border-b-2 border-blue-500 pb-1' : '']"
          >
            Home
          </NuxtLink>
        </li>
        <li>
          <NuxtLink 
            to="/products"
            :class="[$route.path === '/products' ? 'border-b-2 border-blue-500 pb-1' : '']"
          >
            Products
          </NuxtLink>
        </li>
        <li>
          <NuxtLink 
            to="/cart"
            :class="[$route.path === '/cart' ? 'border-b-2 border-blue-500 pb-1' : '']"
          >
            Cart
          </NuxtLink>
        </li>
        <div class="relative" ref="profileDropdown">
          <button 
            @click="toggleProfileDropdown" 
            class="p-1.5 transition-all duration-200 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300"
            :class="[
              ($route.path === '/profile' || isProfileOpen) 
                ? 'bg-blue-500 text-white hover:bg-blue-600' 
                : 'hover:bg-gray-100 text-gray-600'
            ]"
          >
            <span class="material-symbols-outlined text-[20px]">account_circle</span>
          </button>
          
          <transition
            enter-active-class="transition ease-out duration-200"
            enter-from-class="opacity-0 translate-y-1"
            enter-to-class="opacity-100 translate-y-0"
            leave-active-class="transition ease-in duration-150"
            leave-from-class="opacity-100 translate-y-0"
            leave-to-class="opacity-0 translate-y-1"
          >
            <div
              v-if="isProfileOpen"
              class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg py-2 z-50"
              @click.outside="closeProfileDropdown"
            >
            <h4>{{ user?.name }}</h4>
              <NuxtLink
                to="/profile"
                @click="closeProfileDropdown"
                class="block px-4 py-2 text-gray-700 hover:bg-gray-100"
              >
                Profile
              </NuxtLink>
              <NuxtLink
                to="/logout"
                @click="closeProfileDropdown"
                class="block px-4 py-2 text-gray-700 hover:bg-gray-100"
              >
                Logout
              </NuxtLink>
            </div>
          </transition>
        </div>
        
      </ul>
    </div>

    <!-- Dark Background Transition -->
    <Transition name="fade">
      <div
        v-if="isOpen"
        class="z-10 fixed inset-0 bg-black opacity-50"
        @click="closeDrawer"
      ></div>
    </Transition>

    <!-- Drawer Menu -->
    <aside
      class="p-5 fixed top-0 left-0 w-64 bg-white h-full z-30 transition-transform duration-300 shadow-lg rounded-r-lg"
      :class="{ 'translate-x-0': isOpen, '-translate-x-full': !isOpen }"
    >
      <div class="flex justify-end p-4">
        <button @click="closeDrawer" class="text-gray-700 hover:text-gray-900">
          <span class="material-symbols-outlined text-2xl">close</span>
        </button>
      </div>

      <ul class="divide-y font-sans mt-5">
        <li>
          <NuxtLink
            to="/"
            @click="closeDrawer"
            class="block py-3 px-4 hover:bg-gray-100"
            >Home</NuxtLink
          >
        </li>
        <li>
          <NuxtLink
            to="/products"
            @click="closeDrawer"
            class="block py-3 px-4 hover:bg-gray-100"
            >Products</NuxtLink
          >
        </li>
        <li>
          <NuxtLink
            to="/cart"
            @click="closeDrawer"
            class="block py-3 px-4 hover:bg-gray-100"
            >Cart</NuxtLink
          >
        </li>
        <li>
          <NuxtLink as button
            to="/profile"
            @click="closeDrawer"
            class="block py-3 px-4 hover:bg-gray-100"
            > <button class="p-2 rounded-full hover:bg-gray-100 transition-colors">
            <span class="material-symbols-outlined text-gray-600 text-xl">
              person
            </span>
          </button></NuxtLink
          >
        </li>
        <li>
          <NuxtLink
            to="/logout"
            @click="closeDrawer"
            class="block py-3 px-4 hover:bg-gray-100"
            >Logout</NuxtLink
          >
        </li>
          <NuxtLink
            to="/login"
            @click="closeDrawer"
            class="block py-3 px-4 text-center bg-blue-500 hover:bg-blue-600 text-white rounded mt-4"
            >Sign Up
          </NuxtLink>
      </ul>
    </aside>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isOpen: false,
      isProfileOpen: false
    }
  },
  setup() {
    const { $isClient } = useNuxtApp()
    const authStore = useAuthStore()
    if ($isClient) {
      authStore.initAuth() // Initialize auth state on client side
    }
    return {
      authStore
    }
  },
  methods: {
    toggleDrawer() {
      this.isOpen = !this.isOpen
    },
    toggleProfileDropdown() {
      this.isProfileOpen = !this.isProfileOpen
    },
    closeProfileDropdown() {
      this.isProfileOpen = false
    },
    closeDrawer() {
      this.isOpen = false
    },
    handleClickOutside(event) {
      const dropdown = this.$refs.profileDropdown
      if (dropdown && !dropdown.contains(event.target)) {
        this.isProfileOpen = false
      }
    },
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside)
    if (process.client) {
      document.addEventListener("keydown", (e) => {
        if (e.key === "Escape" && this.isOpen) this.closeDrawer();
      });
    }
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleClickOutside)
  },
  watch: {
    isOpen(value) {
      if (useNuxtApp().$isClient) {
        document.body.style.overflow = value ? "hidden" : ""
      }
    },
  },
}
</script>

<style></style>
