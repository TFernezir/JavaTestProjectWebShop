<template>
  <div
    class="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8"
  >
    <div class="sm:mx-auto sm:w-full sm:max-w-sm">
      <img
        class="mx-auto h-10 w-auto"
        src="/images/logo.png"
        alt="Your Company"
      />
      <h2
        class="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900"
      >
        Sign in to your account
      </h2>
    </div>
    <div>
      <LoginForm />
    </div>
    <div>
      <p class="mt-10 text-center text-sm/6 text-gray-500">
        Not a member?
        <NuxtLink
          to="/register"
          class="font-semibold text-indigo-600 hover:text-indigo-500"
          >Register here</NuxtLink
        >
      </p>
    </div>
  </div>
</template>
