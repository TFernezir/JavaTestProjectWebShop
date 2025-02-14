<template>
  <div class="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-sm">
      <img src="/images/logo.png" alt="Logo" class="mx-auto h-10 w-auto" />
      <h2 class="mt-10 text-center text-2xl font-bold">Create account</h2>
    </div>

    <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <RegisterForm />
      <p class="mt-10 text-center text-sm text-gray-500">
        Already registered?
        <NuxtLink to="/login" class="text-indigo-600 hover:text-indigo-500"
          >Sign in</NuxtLink
        >
      </p>
    </div>
  </div>
</template>
