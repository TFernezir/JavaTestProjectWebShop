<template>
  <div v-if="store.isLoading">Loading products...</div>
  <div v-else-if="store.error">Failed to load products: {{ store.error }}</div>
  <div v-else class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8">
    <div v-for="product in store.products" :key="product.id">
      <ProductCard :product="product" />
    </div>
  </div>
</template>

<script setup lang="ts">
const store = useProductStore()

onMounted(async () => {
  await store.getProducts()
})
</script>
