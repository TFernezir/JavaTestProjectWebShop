<template>
  <div
    class="min-h-screen flex flex-col items-center justify-center px-4 md:px-8 text-white text-center"
  >
    <h1 class="text-3xl md:text-4xl font-bold mb-4">Welcome to Our Webshop</h1>
    <p class="text-base md:text-lg mb-6">
      Discover the best products at unbeatable prices.
    </p>
    <NuxtLink
      to="/products"
      class="px-4 md:px-6 py-2 md:py-3 bg-primary text-white rounded-xl shadow-lg hover:bg-opacity-80 transition text-sm md:text-base"
    >
      Shop Now
    </NuxtLink>
  </div>
</template>

<script setup></script>

<style scoped>
.bg-primary {
  background-color: #ff6600; /* Customize the button color */
}
</style>
