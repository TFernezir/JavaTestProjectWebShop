<template>
  <div>
    <p>Logging out...</p>
  </div>
</template>

<script setup>
const authStore = useAuthStore();

authStore.logout();
</script>
