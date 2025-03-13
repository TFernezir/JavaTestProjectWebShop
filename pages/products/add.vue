<template>
  <div class="container mx-auto px-4 py-8 flex justify-center">
    <div v-if="!isAdmin" class="text-center">
      <h2 class="text-xl text-red-600">Access Denied</h2>
      <p class="mt-2">You need admin privileges to access this page.</p>
      <NuxtLink to="/products" class="text-blue-600 hover:underline mt-4 inline-block">
        Back to Products
      </NuxtLink>
    </div>

    <div v-else class="w-full max-w-2xl">
      <h1 class="text-2xl font-bold mb-6">Add New Product</h1>
      
      <form @submit.prevent="handleSubmit" class="bg-white p-6 rounded-lg shadow-md">
        <div class="space-y-6">
          <!-- Name -->
          <div>
            <label for="name" class="block text-sm font-medium text-gray-700">Product Name</label>
            <input
              id="name"
              v-model="product.name"
              type="text"
              required
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            />
          </div>

          <!-- Price -->
          <div>
            <label for="price" class="block text-sm font-medium text-gray-700">Price</label>
            <input
              id="price"
              v-model="product.price"
              type="number"
              step="0.01"
              required
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            />
          </div>

          <!-- Category -->
          <div>
            <label for="category" class="block text-sm font-medium text-gray-700">Category</label>
            <select
              id="category"
              v-model="product.categoryName"
              required
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            >
              <option v-for="category in categories" :key="category" :value="category">
                {{ category }}
              </option>
            </select>
          </div>

          <!-- About -->
          <div>
            <label for="about" class="block text-sm font-medium text-gray-700">Description</label>
            <textarea
              id="about"
              v-model="product.about"
              rows="4"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            ></textarea>
          </div>

          <!-- Image Upload -->
          <div>
            <label for="image" class="block text-sm font-medium text-gray-700">Product Image</label>
            <input
              id="image"
              type="file"
              @change="handleImageChange"
              accept="image/*"
              class="mt-1 block w-full text-sm text-gray-500"
            />
          </div>

          <div class="flex justify-end gap-4">
            <NuxtLink
              to="/products"
              class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
            >
              Cancel
            </NuxtLink>
            <button
              type="submit"
              :disabled="isLoading"
              class="px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-700 disabled:opacity-50"
            >
              {{ isLoading ? 'Adding...' : 'Add Product' }}
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '~/stores/authStore';
import { useProductStore } from '~/stores/productStore';

const authStore = useAuthStore();
const productStore = useProductStore();

// Check if user is admin
const isAdmin = computed(() => {
  return authStore.user?.role?.includes('ROLE_ADMIN') ?? false;
});

// Basic product form state
const product = ref({
  name: '',
  price: 0,
  about: '',
  categoryName: '',
});

// Image file storage
const imageFile = ref(null);

// Loading state
const isLoading = ref(false);

// Categories
const categories = ref(['Electronics', 'Books', 'Clothing']); // Replace with fetched categories

// Handle image change
const handleImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    imageFile.value = file;
  }
};

// Handle form submission
const handleSubmit = async () => {
  try {
    isLoading.value = true;
    // Convert price to number
    const productData = {
      ...product.value,
      price: Number(product.value.price)
    };
    
    console.log('Submitting product:', productData);
    console.log('Image file:', imageFile.value);
    
    await productStore.addProduct(productData, imageFile.value);
    alert('Product added successfully');
    // Redirect to products page
    // navigateTo('/products');
  } catch (error) {
    console.error('Error adding product:', error);
    alert('Failed to add product. Please try again.');
  } finally {
    isLoading.value = false;
  }
};

// Fetch categories from backend
// onMounted(async () => {
//   // Fetch categories from backend and set to categories ref
//   // Example: categories.value = await fetchCategories();
// });

// Log the current user role for testing
console.log('Current user roles:', authStore.user?.role);
console.log('Is admin:', isAdmin.value);
</script>