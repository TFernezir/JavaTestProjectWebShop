<template>
  <div
    class="card max-w-xl flex items-center "
  >
      <NuxtLink :to="`/products/${product.id}`">
        <img class="rounded-t-lg" :src="product.img" :alt="product.name" />
      </NuxtLink>
    <div class="p-5">
        <NuxtLink :to="`/products/${product.id}`">
                  <h5
          class="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white"
        >
          {{ product.name }}
        </h5>
        </NuxtLink>
      <p class="mb-3 font-normal text-gray-700 dark:text-gray-400">
        {{ product.about }}
      </p>
      <p class="text-lg text-gray-500">{{ product.price }} Euro</p>
      <button class="btn text-contrast mt-4 sm:mt-0">
        <span> Add to cart </span>
      </button>
    </div>
  </div>
</template>

<script setup>
const { product } = defineProps(["product"]);
</script>
