// src/services/api.service.js
import axios from 'axios';

// Create axios instance with base configuration
const apiClient = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  timeout: 10000
});

// Add a request interceptor for auth tokens if needed
apiClient.interceptors.request.use(
  config => {
    // You can add auth token here if needed
    const token = localStorage.getItem('auth_token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// Add a response interceptor for error handling
apiClient.interceptors.response.use(
  response => response,
  error => {
    // Global error handling
    console.error('API Error:', error.response || error);
    return Promise.reject(error);
  }
);

export default apiClient;