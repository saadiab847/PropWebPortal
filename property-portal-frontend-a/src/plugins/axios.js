import axios from 'axios'

// Base URL configuration
axios.defaults.baseURL = process.env.VUE_APP_API_URL || 'http://localhost:8080/api'

// Request interceptor
axios.interceptors.request.use(
  config => {
    // Add any request modifications here (auth tokens, headers, etc.)
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// Response interceptor
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    // Handle global error responses here
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export default axios