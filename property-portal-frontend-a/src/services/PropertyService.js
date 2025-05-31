// src/services/PropertyService.js
import axios from 'axios';

const API_URL = '/api';

export default {
  getProperties(page = 0, size = 10, filters = {}) {
    let queryParams = `?page=${page}&size=${size}`;
    
    // Add any filters to the query parameters
    if (filters.location) queryParams += `&location=${filters.location}`;
    if (filters.minPrice) queryParams += `&minPrice=${filters.minPrice}`;
    if (filters.maxPrice) queryParams += `&maxPrice=${filters.maxPrice}`;
    if (filters.propertyType) queryParams += `&propertyType=${filters.propertyType}`;
    
    return axios.get(`${API_URL}/properties${queryParams}`);
  },
  
  getPropertyById(id) {
    return axios.get(`${API_URL}/properties/${id}`);
  }
}