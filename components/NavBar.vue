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
    <div class="hidden md:block">
      <ul class="flex space-x-8 text-sm font-sans">
        <li>
          <NuxtLink to="/" class="active border-b-2 border-blue-500 pb-1"
            >Home</NuxtLink
          >
        </li>
        <li><NuxtLink to="/products">Products</NuxtLink></li>
        <li><NuxtLink to="/cart">Cart</NuxtLink></li>
        <li><NuxtLink to="/profile">Profile</NuxtLink></li>
        <li><NuxtLink to="/logout">Logout</NuxtLink></li>
        <li>
          <NuxtLink
            to="/login"
            class="cta bg-blue-500 hover:bg-blue-600 px-3 py-2 rounded text-white font-semibold"
            >Sign Up</NuxtLink
          >
        </li>
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
          <NuxtLink
            to="/profile"
            @click="closeDrawer"
            class="block py-3 px-4 hover:bg-gray-100"
            >Profile</NuxtLink
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
        <li>
          <NuxtLink
            to="/login"
            @click="closeDrawer"
            class="block py-3 px-4 text-center bg-blue-500 hover:bg-blue-600 text-white rounded mt-4"
            >Sign Up</NuxtLink
          >
        </li>
      </ul>
    </aside>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isOpen: false,
    };
  },
  methods: {
    toggleDrawer() {
      this.isOpen = !this.isOpen;
    },
    closeDrawer() {
      this.isOpen = false;
    },
  },
  watch: {
    isOpen(value) {
      if (process.client) {
        document.body.style.overflow = value ? "hidden" : "";
      }
    },
  },
  mounted() {
    if (process.client) {
      document.addEventListener("keydown", (e) => {
        if (e.key === "Escape" && this.isOpen) this.closeDrawer();
      });
    }
  },
};
</script>

<style></style>
