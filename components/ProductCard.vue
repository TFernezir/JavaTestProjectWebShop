<template>
  <div
    class="card max-w-xl flex items-center "
  >
    <a href="#">
      <img class="rounded-t-lg" :src="product.img" :alt="product.title" />
    </a>
    <div class="p-5">
      <a href="#">
        <h5
          class="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white"
        >
          {{ product.title }}
        </h5>
      </a>
      <p class="mb-3 font-normal text-gray-700 dark:text-gray-400">
        {{ product.description }}
      </p>
      <p class="text-lg text-gray-500">{{ product.price }} Euro</p>
      <button class="btn text-contrast mt-4 sm:mt-0">
        <span> Add to basket </span>
      </button>
    </div>
  </div>
</template>

<script setup>
const { product } = defineProps(["product"]);
</script>
