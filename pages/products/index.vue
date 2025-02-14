<template>
  <div v-if="isLoading">Loading products...</div>
  <div v-else-if="error">Failed to load products: {{ error.message }}</div>
  <div v-else class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8">
    <div v-for="product in products" :key="product.id">
      <ProductCard :product="product" />
    </div>
  </div>
</template>

<script setup>
const {
  data: products,
  status,
  error,
} = await useFetch("http://localhost:4000/products");

const isLoading = computed(() => status.value === "pending");
</script>
