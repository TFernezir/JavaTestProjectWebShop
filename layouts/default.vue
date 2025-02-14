<template>
  <div v-if="!isLoginOrRegister">
    <Navbar />
    <div>
      <div class="wrapper text-secondary">
        <slot />
      </div>
    </div>
  </div>
  <div v-else>
    <div>
      <slot />
    </div>
  </div>
</template>

<script setup>
import { useRoute } from "vue-router";
import Navbar from "@/components/Navbar.vue";
import { useCartStore } from "@/stores/cartStore";

const cartStore = useCartStore();

const route = useRoute();
const isLoginOrRegister = computed(
  () => route.path === "/login" || route.path === "/register"
);

cartStore.getCart();
</script>

<style></style>
