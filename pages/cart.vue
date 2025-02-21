<template>
  <div>
    <div class="card text-xl" v-if="cartStore.cart.length">
      <!-- product list -->
      <div v-for="product in cartStore.cart" class="flex gap-8 items-center">
        <img :src="product.img" :alt="product.name" />
        <a href="#">
          <h5
            class="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white"
          >
            {{ product.name }}
          </h5>
        </a>
        <p class="mb-3 font-normal text-gray-700 dark:text-gray-400">
          {{ product.about }}
        </p>
        <p class="text-xlg text-yelow-500">
          {{ product.price * product.quantity }} Euro
        </p>
      </div>
    </div>
    <div v-else></div>
  </div>
</template>

<script setup>
const cartStore = useCartStore();
</script>

<style scoped></style>
