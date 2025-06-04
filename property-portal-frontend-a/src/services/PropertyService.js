// src/services/PropertyService.js
import axios from 'axios';

const API_URL = '/api';

export default {
  getProperties(page = 0, size = 10, filters = {}) {
    let queryParams = `?page=${page}&size=${size}`;
    
    // Add any filters to the query parameters
    if (filters.location) queryParams += `&city=${filters.location}`;
    if (filters.propertyType) queryParams += `&type=${filters.propertyType}`;
    if (filters.isAvailable) queryParams += `&available=true`;
    
    return axios.get(`${API_URL}/properties${queryParams}`);
  },
  
  getPropertyById(id) {
    return axios.get(`${API_URL}/properties/${id}`);
  },
  // Add these functions to your existing PropertyService.js file

/**
 * Search properties by specific criteria
 * @param {string} criteria - The search criteria (id, type, city, availability)
 * @param {string} query - The search query
 * @param {number} page - Page number (0-based)
 * @param {number} size - Page size
 * @returns {Promise} - API response promise
 */
searchPropertiesByCriteria(criteria, query, page = 0, size = 12) {
  if (criteria === 'none' || !query) {
    return this.getProperties(page, size);
  }
  
  const searchParams = {};
  searchParams[criteria] = query;
  
  return this.getProperties(page, size, searchParams);
},

/*
 * Search properties by specific criteria (id, type, city, availability)
 * @param {string} criteria - The search criteria (id, type, city, availability)
 * @param {string} query - The search query value
 * @param {number} page - Current page number (0-based for API)
 * @param {number} size - Number of results per page
 * @returns {Promise} - Promise with search results
 */
searchProperties(criteria, query, page = 0, size = 10) {
  const searchParams = {};
  
  // Only add search parameter if criteria and query are provided
  if (criteria && criteria !== 'none' && query) {
    // Map frontend search criteria to backend API parameters
    switch (criteria) {
      case 'id':
        searchParams.id = query;
        break;
      case 'type':
        searchParams.propertyType = query;
        break;
      case 'city':
        searchParams.city = query;
        break;
      case 'availability':
        searchParams.available = query.toLowerCase() === 'true' || query.toLowerCase() === 'yes';
        break;
    }
  }
  
  return axios.get('/api/properties/search', {
    params: {
      page,
      size,
      ...searchParams
    }
  });
},

/**
 * Get properties with combined filters and search parameters
 * @param {number} page - Current page number (0-based for API)
 * @param {number} size - Number of results per page
 * @param {Object} filters - Filter parameters
 * @param {Object} searchParams - Search parameters
 * @returns {Promise} - Promise with property results
 */
getPropertiesWithSearch(page = 0, size = 10, filters = {}, searchParams = {}) {
  // Combine filters and search parameters
  const params = {
    page,
    size,
    ...filters,
    ...searchParams
  };
  
  // Log the search request for debugging
  console.log('Searching properties with parameters:', params);
  
  return axios.get('/api/properties', {
    params
  });
}
}