<template>
  <div class="container mx-auto px-4 py-8">
    <div class="flex justify-between items-center mb-8">
      <div class="flex gap-4">
        <!-- Add filters or search here if needed -->
      </div>
    </div>

    <div v-if="productStore.isLoading" class="flex justify-center items-center min-h-[400px]">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
    </div>
    
    <div v-else-if="productStore.error" class="text-center py-8">
      <p class="text-red-500">{{ productStore.error }}</p>
    </div>

    <div v-else class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-6 gap-4">
      <ProductCard 
        v-for="product in productStore.products" 
        :key="product.id" 
        :product="product"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
const productStore = useProductStore();

onMounted(async () => {
  await productStore.getProducts();
});
</script>
