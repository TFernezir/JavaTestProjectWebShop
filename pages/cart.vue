<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">Shopping Cart</h1>
    
    <div v-if="cartStore.isLoading && !cartStore.basket" class="text-center py-4">
      <p>Loading your cart...</p>
    </div>

    <div v-else-if="cartStore.error" class="text-red-500 text-center py-4 bg-red-50 rounded-lg">
      {{ cartStore.error }}
    </div>

    <div v-else>
      <div v-if="cartStore.cartItems.length" class="space-y-4">
        <div class="bg-white rounded-lg shadow p-4">
          <div v-for="item in cartStore.cartItems" :key="item.id" class="flex items-center gap-4 py-4 border-b last:border-0">
            <div class="w-20 h-20 rounded overflow-hidden">
              <img 
                v-if="item.product && item.product.id && !getImageError(item.product.id)"
                :src="getProductImageUrl(item.product.id)" 
                :alt="item.product.name" 
                class="w-full h-full object-cover"
                @error="handleImageError(item.product.id)" 
              />
              <div 
                v-else
                class="w-full h-full flex items-center justify-center bg-gray-100"
              >
                <span class="text-gray-400 text-xs">No image</span>
              </div>
            </div>
            <div class="flex-grow">
              <h5 class="text-lg font-semibold text-gray-900">{{ item.product.name }}</h5>
              <p class="text-gray-600">{{ item.product.about }}</p>
            </div>
            <div class="text-right flex flex-col items-end gap-2">
              <p class="text-lg font-semibold text-yellow-600">
                {{ (item.unitPrice * item.quantity).toFixed(2) }} Euro
              </p>
              <div class="flex items-center gap-2">
                <button 
                  @click="updateQuantity(item, item.quantity - 1)"
                  :disabled="cartStore.isProductLoading(item.product.id)"
                  class="bg-red-500 hover:bg-red-600 disabled:bg-red-300 text-white px-3 py-1 rounded flex items-center gap-1"
                >
                  <span class="text-sm">-</span>
                </button>
                <span class="text-sm font-medium w-8 text-center">{{ item.quantity }}</span>
                <button 
                  @click="updateQuantity(item, item.quantity + 1)"
                  :disabled="cartStore.isProductLoading(item.product.id)"
                  class="bg-green-500 hover:bg-green-600 disabled:bg-green-300 text-white px-3 py-1 rounded flex items-center gap-1"
                >
                  <span class="text-sm">+</span>
                </button>
              </div>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow p-4 mt-4">
          <div class="flex justify-between items-center">
            <h3 class="text-xl font-semibold">Total</h3>
            <p class="text-xl font-bold text-yellow-600">{{ cartStore.totalAmount.toFixed(2) }} Euro</p>
          </div>
        </div>
      </div>
      <div v-else class="text-center py-8 text-gray-500">
        Your cart is empty
      </div>
    </div>
  </div>
</template>

<script setup>

const cartStore = useCartStore();
const imageErrors = ref({});
const imageUrls = ref({});

onMounted(async () => {
  await cartStore.getCart();
  // Load images for all products in cart
  if (cartStore.cartItems && cartStore.cartItems.length) {
    cartStore.cartItems.forEach(item => {
      if (item.product && item.product.id) {
        loadProductImage(item.product.id);
      }
    });
  }
});

// Load the product image with authentication
const loadProductImage = async (productId) => {
  try {
    const token = localStorage.getItem('authToken');
    if (!token) {
      imageErrors.value[productId] = true;
      return;
    }
    
    const response = await fetch(`http://localhost:5001/products/${productId}/image`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    if (!response.ok) {
      throw new Error(`Failed to load image: ${response.status}`);
    }
    
    const blob = await response.blob();
    imageUrls.value[productId] = URL.createObjectURL(blob);
  } catch (error) {
    console.error('Error loading image:', error);
    imageErrors.value[productId] = true;
  }
};

const getProductImageUrl = (productId) => {
  return imageUrls.value[productId] || null;
};

const handleImageError = (productId) => {
  imageErrors.value[productId] = true;
};

const getImageError = (productId) => {
  return imageErrors.value[productId] || false;
};

const updateQuantity = async (item, newQuantity) => {
  if (newQuantity === 0) {
    await cartStore.removeFromCart(item.id);
  } else {
    await cartStore.addToCart(item.product.id, newQuantity);
  }
};
</script>
