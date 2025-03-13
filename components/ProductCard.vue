<template>
  <div class="h-full bg-white rounded-lg shadow-md overflow-hidden flex flex-col">
    <NuxtLink :to="`/products/${product.id}`" class="block aspect-square overflow-hidden">
      <img 
        v-if="!imageError && imageUrl"
        class="w-full h-full object-cover hover:scale-105 transition-transform duration-300" 
        :src="imageUrl" 
        :alt="product.name" 
        @error="handleImageError"
      />
      <div 
        v-else
        class="w-full h-full flex items-center justify-center bg-gray-100"
      >
        <span class="text-gray-400">No image</span>
      </div>
    </NuxtLink>
    <div class="p-3 flex flex-col flex-grow">
      <NuxtLink :to="`/products/${product.id}`">
        <h5 class="mb-1.5 text-base font-bold tracking-tight text-gray-900 dark:text-white line-clamp-1 hover:text-indigo-600">
          {{ product.name }}
        </h5>
      </NuxtLink>
      <p class="mb-2 text-sm font-normal text-gray-700 dark:text-gray-400 line-clamp-2 flex-grow">
        {{ product.about }}
      </p>
      <div class="mt-auto space-y-2">
        <p class="text-base font-semibold text-indigo-600">{{ product.price.toFixed(2) }} Euro</p>
        <button 
          @click="addToCart"
          :disabled="cartStore.isProductLoading(product.id)"
          class="w-full bg-indigo-600 text-white px-3 py-1.5 rounded-lg text-sm font-semibold hover:bg-indigo-700 transition-colors disabled:bg-indigo-300 flex items-center justify-center"
        >
          <span v-if="cartStore.isProductLoading(product.id)">Adding...</span>
          <span v-else>Add to cart</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
const { product } = defineProps(["product"]);
const cartStore = useCartStore();

const imageError = ref(false);
const imageUrl = ref(null);

// Load the image with authentication
const loadImage = async () => {
  if (!product.id) return;
  
  try {
    const token = localStorage.getItem('authToken');
    if (!token) {
      imageError.value = true;
      return;
    }
    
    const response = await fetch(`http://localhost:5001/products/${product.id}/image`, {
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

const addToCart = async () => {
  await cartStore.addToCart(product.id, 1);
};
</script>
