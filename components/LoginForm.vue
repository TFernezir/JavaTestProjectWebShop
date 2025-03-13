<template>
  <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
    <form @submit.prevent="handleSubmit" class="space-y-6">
      <div v-if="errorMessage" class="rounded-md bg-red-50 p-4 mb-4">
        <div class="flex">
          <div class="flex-shrink-0">
            <svg class="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.28 7.22a.75.75 0 00-1.06 1.06L8.94 10l-1.72 1.72a.75.75 0 101.06 1.06L10 11.06l1.72 1.72a.75.75 0 101.06-1.06L11.06 10l1.72-1.72a.75.75 0 00-1.06-1.06L10 8.94 8.28 7.22z" clip-rule="evenodd" />
            </svg>
          </div>
          <div class="ml-3">
            <h3 class="text-sm font-medium text-red-800">{{ errorMessage }}</h3>
          </div>
        </div>
      </div>

      <div>
        <label for="email" class="block text-sm/6 font-medium text-gray-900">
          Email address
        </label>
        <div class="mt-2">
          <input
            v-model="form.email"
            type="email"
            id="email"
            required
            class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
          />
        </div>
      </div>

      <div>
        <div class="flex items-center justify-between">
          <label
            for="password"
            class="block text-sm/6 font-medium text-gray-900"
          >
            Password
          </label>
          <div class="text-sm">
            <a
              href="#"
              class="font-semibold text-indigo-600 hover:text-indigo-500"
            >
              Forgot password?
            </a>
          </div>
        </div>
        <div class="mt-2">
          <input
            v-model="form.password"
            type="password"
            id="password"
            required
            class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
          />
        </div>
      </div>

      <button
        type="submit"
        :disabled="form.isLoading"
        class="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        <span v-if="form.isLoading">Signing in...</span>
        <span v-else>Sign in</span>
      </button>
    </form>
  </div>
</template>

<script setup>
const authStore = useAuthStore();
const errorMessage = ref('');

onMounted(() => {
  if (typeof window !== 'undefined') {
    // Ensure token is not cleared unnecessarily
    if (!localStorage.getItem('authToken')) {
      localStorage.setItem('authToken', '');
    }
  }
});

const form = reactive({
  email: "",
  password: "",
  isLoading: false,
});

const handleSubmit = async () => {
  if (form.isLoading) return;
  
  errorMessage.value = ''; // Clear any previous errors
  form.isLoading = true;
  
  try {
    const dto = {
      email: form.email,
      password: form.password,
    };

    const response = await authStore.login(dto);
    if (response) {
      await navigateTo("/");
    }
  } catch (error) {
    errorMessage.value = error.message || 'An error occurred during login';
  } finally {
    form.isLoading = false;
  }
};
</script>
