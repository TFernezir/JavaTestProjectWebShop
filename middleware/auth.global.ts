export default defineNuxtRouteMiddleware((to, from) => {
  if (typeof window === 'undefined') return; // Exit if not on client side

  const publicRoutes = ['/', '/login', '/register'];
  const token = localStorage.getItem('authToken');

  console.log("Middleware: Navigating to", to.path);
  console.log("Middleware: Token", token);

  if (!publicRoutes.includes(to.path) && !token) {
    console.log("Middleware: Redirecting to login");
    return navigateTo('/login');
  }
});