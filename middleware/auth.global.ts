export default defineNuxtRouteMiddleware((to, from) => {
  if (typeof window === 'undefined') return; // Exit if not on client side

  const publicRoutes = ['/', '/login', '/register'];
  const token = localStorage.getItem('authToken');

  if (!publicRoutes.includes(to.path) && !token) {
    return navigateTo('/login');
  }
});