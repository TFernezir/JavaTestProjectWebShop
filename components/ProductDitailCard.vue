<template>
  <div class=" card max-w-4xl mx-auto rounded-lg shadow-lg overflow-hidden">
    <div class="md:flex">
      <div class="md:flex-shrink-0">
        <div class="h-96 w-full md:w-96 relative">
          <img
            v-if="!imageError && imageUrl"
            class="h-full w-full object-cover"
            :src="imageUrl"
            :alt="product.name"
            @error="handleImageError"
          />
          <div 
            v-else
            class="h-full w-full flex items-center justify-center bg-gray-100"
          >
            <span class="text-gray-400">No image available</span>
          </div>
        </div>
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
            class="flex-1 bg-indigo-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-indigo-700 transition-colors disabled:bg-indigo-300"
            :disabled="!product.available || cartStore.isProductLoading(product.id)"
            @click="addToCart"
          >
            <span v-if="cartStore.isProductLoading(product.id)">Adding to Cart...</span>
            <span v-else>Add to Cart - {{ (product.price * quantity).toFixed(2) }} Euro</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Product } from '~/types/product'

const props = defineProps<{
  product: Product
}>()

const cartStore = useCartStore()
const quantity = ref(1)
const imageError = ref(false)
const imageUrl = ref<string | undefined>(undefined)

// Load the image with authentication
const loadImage = async () => {
  if (!props.product.id) return;
  
  try {
    const token = localStorage.getItem('authToken');
    if (!token) {
      imageError.value = true;
      return;
    }
    
    const response = await fetch(`http://localhost:5001/products/${props.product.id}/image`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    if (!response.ok) {
      throw new Error(`Failed to load image: ${response.status}`);
    }
    
    const blob = await response.blob();
    imageUrl.value = URL.createObjectURL(blob);
  } catch (error) {
    console.error('Error loading image:', error);
    imageError.value = true;
  }
};

onMounted(() => {
  loadImage();
});

const handleImageError = () => {
  imageError.value = true;
};

const incrementQuantity = () => {
  quantity.value++
}

const decrementQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const addToCart = async () => {
  await cartStore.addToCart(props.product.id, quantity.value)
}
</script>

<style>

</style>