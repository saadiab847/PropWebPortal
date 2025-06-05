// PropertyService.js
import axios from 'axios';

const API_URL = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080/api';

class PropertyService {
  // Existing getProperties method with enhanced search capability
  getProperties(page = 0, size = 12, params = {}) {
    console.log('PropertyService: Fetching properties with params:', { page, size, ...params });
    
    const queryParams = {
      page,
      size,
      ...params
    };
    
    return axios.get(`${API_URL}/properties`, { 
      params: queryParams
    });
  }
  
  // New method for searching properties by specific criteria
  searchProperties(searchCriteria, searchQuery, page = 0, size = 12) {
    console.log(`PropertyService: Searching properties by ${searchCriteria}: ${searchQuery}`);
    
    let queryParams = {
      page,
      size
    };
    
    // Add search parameter based on criteria
    if (searchCriteria && searchCriteria !== 'none' && searchQuery) {
      queryParams[searchCriteria] = searchQuery;
    }
    
    return axios.get(`${API_URL}/properties/search`, {
      params: queryParams
    });
  }
  
  // New method for advanced property search with multiple criteria
  advancedSearch(searchParams, page = 0, size = 12) {
    console.log('PropertyService: Advanced search with params:', searchParams);
    
    const queryParams = {
      page,
      size,
      ...searchParams
    };
    
    return axios.get(`${API_URL}/properties/advanced-search`, {
      params: queryParams
    });
  }
}

export default new PropertyService();