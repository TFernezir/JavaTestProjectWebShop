<template>
  <div class="container mx-auto px-4 py-8">
    <div v-if="store.isLoading" class="text-center py-12">
      Loading product...
    </div>
    <div v-else-if="store.error" class="text-center py-12 text-red-600">
      {{ store.error }}
    </div>
    <ProductDitailCard v-else :product="store.product" />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useProductStore } from '~/stores/productStore'

const route = useRoute()
const store = useProductStore()

onMounted(async () => {
  await store.getProduct(Number(route.params.id))
})
</script>

<style></style>