<template>
  <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
    <form @submit.prevent="handleSubmit" class="space-y-6">
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
        class="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
      >
        Sign in
      </button>
    </form>
  </div>
</template>

<script setup>
const authStore = useAuthStore();

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
    console.error(error);
  }
};
</script>
