export default defineNuxtRouteMiddleware((to, from) => {
  if (typeof window === 'undefined') return; // Exit if not on client side

  const publicRoutes = ['/', '/login', '/register']; // Products require auth as per backend config
  const token = localStorage.getItem('authToken');
  
  // Check if token exists and is not expired
  const isValidToken = () => {
    if (!token) return false;
    try {
      const tokenData = JSON.parse(atob(token.split('.')[1]));
      const isExpired = tokenData.exp * 1000 <= Date.now();
      if (isExpired) {
        localStorage.removeItem('authToken'); // Clear expired token
        return false;
      }
      return true;
    } catch {
      localStorage.removeItem('authToken'); // Clear invalid token
      return false;
    }
  };

  // Redirect to login for protected routes
  if (!publicRoutes.includes(to.path) && !isValidToken()) {
    return navigateTo('/login');
  }
});