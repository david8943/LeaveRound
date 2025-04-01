import axios from 'axios';

const api = axios.create({
  baseURL: `${import.meta.env.VITE_BASE_URL}`,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const errorData = error.response?.data || { success: false, message: 'Unknown error' };

    return Promise.reject(errorData);
  },
);

export default api;
