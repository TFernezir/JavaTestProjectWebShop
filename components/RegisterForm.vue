<template>
  <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
    <form @submit.prevent="register" class="space-y-6">
      <div>
        <label for="userName" class="block text-sm/6 font-medium text-gray-900">
          User name
        </label>
        <div class="mt-2">
          <input
            v-model="form.userName"
            @input="handleInput('userName')"
            type="text"
            id="userName"
            required
            :class="[
              'block w-full rounded-md px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 focus:outline-2 focus:-outline-offset-2 sm:text-sm/6',
              form.touched.email && errors.email
                ? 'bg-red-50 outline-red-600'
                : 'bg-indigo-50 outline-indigo-600',
            ]"
          />
          <p
            v-if="form.touched.userName && errors.userName"
            class="mt-1 text-sm text-red-600"
          >
            {{ errors.userName }}
          </p>
        </div>
      </div>

      <div>
        <label for="email" class="block text-sm/6 font-medium text-gray-900">
          Email address
        </label>
        <div class="mt-2">
          <input
            v-model="form.email"
            @input="handleInput('email')"
            type="email"
            id="email"
            required
            :class="[
              'block w-full rounded-md px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 focus:outline-2 focus:-outline-offset-2 sm:text-sm/6',
              form.touched.email && errors.email
                ? 'bg-red-50 outline-red-600'
                : 'bg-indigo-50 outline-indigo-600',
            ]"
          />
          <p
            v-if="form.touched.email && errors.email"
            class="mt-1 text-sm text-red-600"
          >
            {{ errors.email }}
          </p>
        </div>
      </div>

      <div>
        <label for="password" class="block text-sm/6 font-medium text-gray-900">
          Password
        </label>
        <div class="mt-2">
          <input
            v-model="form.password"
            @input="handleInput('password')"
            type="password"
            id="password"
            required
            :class="[
              'block w-full rounded-md px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 focus:outline-2 focus:-outline-offset-2 sm:text-sm/6',
              form.touched.password && errors.password
                ? 'bg-red-50 outline-red-600'
                : 'bg-indigo-50 outline-indigo-600',
            ]"
          />
          <p
            v-if="form.touched.password && errors.password"
            class="mt-1 text-sm text-red-600"
          >
            {{ errors.password }}
          </p>
        </div>
      </div>

      <div>
        <label
          for="confirmPassword"
          class="block text-sm/6 font-medium text-gray-900"
        >
          Confirm Password
        </label>
        <div class="mt-2">
          <input
            v-model="form.confirmPassword"
            @input="handleInput('confirmPassword')"
            type="password"
            id="confirmPassword"
            required
            :class="[
              'block w-full rounded-md px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 focus:outline-2 focus:-outline-offset-2 sm:text-sm/6',
              form.touched.confirmPassword && errors.confirmPassword
                ? 'bg-red-50 outline-red-600'
                : 'bg-indigo-50 outline-indigo-600',
            ]"
          />
          <p
            v-if="form.touched.confirmPassword && errors.confirmPassword"
            class="mt-1 text-sm text-red-600"
          >
            {{ errors.confirmPassword }}
          </p>
        </div>
      </div>

      <button
        type="submit"
        :disabled="!isValid"
        :class="[
          'flex w-full justify-center rounded-md px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs focus-visible:outline-2 focus-visible:outline-offset-2',
          !isValid ? 'bg-gray-400' : 'bg-indigo-600 hover:bg-indigo-500',
        ]"
      >
        Register
      </button>
    </form>
  </div>
</template>

<script setup>
const authStore = useAuthStore();

const form = reactive({
  userName: "",
  email: "",
  password: "",
  confirmPassword: "",
  touched: {
    userName: false,
    email: false,
    password: false,
    confirmPassword: false,
  },
});

const errors = computed(() => ({
  userName:
    form.touched.userName && !form.userName ? "Username is required" : "",
  email: !/.+@.+\..+/.test(form.email) ? "Invalid email address" : "",
  password:
    form.password.length < 8 ? "Password must be at least 8 characters" : "",
  confirmPassword:
    form.password !== form.confirmPassword ? "Passwords don't match" : "",
}));

const isValid = computed(() => {
  return (
    form.email &&
    form.password &&
    form.confirmPassword &&
    !errors.value.email &&
    !errors.value.password &&
    !errors.value.confirmPassword
  );
});

const handleInput = (field) => {
  form.touched[field] = true;
};

const register = async () => {
  try {
    const dto = {
      userName: form.userName,
      email: form.email,
      password: form.password,
    };
    const response = await authStore.register(dto);
    if (response === "User registered successfully") {
      await navigateTo("/login");
    }
  } catch (error) {
    console.error(error);
  }
};
</script>
