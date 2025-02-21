<template>
  <div class=" card max-w-4xl mx-auto rounded-lg shadow-lg overflow-hidden">
    <div class="md:flex">
      <div class="md:flex-shrink-0">
        <img
          class="h-96 w-full object-cover md:w-96"
          :src="product.img"
          :alt="product.name"
        />
      </div>
      <div class="p-8">
        <div class="flex justify-between items-start">
          <div>
            <h2 class="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
              {{ product.name }}
            </h2>
            <p class="text-xl font-semibold text-secondary mb-4">
              {{ product.price }} Euro
            </p>
          </div>
          <span
            :class="[
              'px-3 py-1 rounded-full text-sm font-semibold',
              product.available
                ? 'bg-green-200 text-green-800'
                : 'bg-red-200 text-red-800',
            ]"
          >
            {{ product.available ? "In Stock" : "Out of Stock" }}
          </span>
        </div>

        <div class="mt-4">
          <span class="text-gray-900 dark:text-white">Category:</span>
          <span class="ml-2 px-3 py-1 bg-gray-100 rounded-full text-sm font-medium text-gray-800">
            {{ product.category }}
          </span>
        </div>

        <div class="mt-6">
          <h3 class="text-lg font-semibold text-gray-900 dark:text-white mb-2">Description</h3>
          <p class="text-gray-600 dark:text-white text-lg leading-relaxed">
            {{ product.about }}
          </p>
        </div>

        <div class="mt-8 flex items-center gap-4">
          <div class="flex items-center border rounded-lg px-3 bg-secondDark dark:bg-gray-100">
            <button
              class="py-2 px-3 text-gray-600 hover:text-gray-700 text-xl"
              @click="decrementQuantity"
            >
              -
            </button>
            <span class=" py-2 px-3 text-gray-900 text-lg">{{ quantity }}</span>
            <button
              class="py-2 px-3 text-gray-600 hover:text-gray-700 text-xl"
              @click="incrementQuantity"
            >
              +
            </button>
          </div>
          <button
            class="flex-1 bg-indigo-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-indigo-700 transition-colors"
            :disabled="!product.available"
            @click="addToCart"
          >
            Add to Cart - {{ product.price * quantity }} Euro
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useCartStore } from '~/stores/cartStore'
import type { Product } from '~/types/product'

const props = defineProps<{
  product: Product
}>()

const cartStore = useCartStore()
const quantity = ref(1)

const incrementQuantity = () => {
  quantity.value++
}

const decrementQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const addToCart = () => {
  // cartStore.addToCart({
  //   ...props.product,
  //   quantity: quantity.value
  // })
}
</script>

<style>

</style>