export const isAuthenticated = () => {
  return sessionStorage.getItem('isLoggedIn') === 'true';
};
