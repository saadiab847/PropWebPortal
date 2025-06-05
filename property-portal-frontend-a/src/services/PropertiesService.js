import axios from 'axios'

const PropertiesService = {
  /**
   * Fetch properties with optional filtering, sorting and pagination
   */
  async getProperties(options = {}) {
    try {
      const response = await axios.get('/properties', { params: options })
      return {
        data: response.data,
        error: null
      }
    } catch (error) {
      console.error('Error fetching properties:', error)
      return {
        data: null,
        error: error.response?.data?.message || 'Failed to fetch properties'
      }
    }
  },

  /**
   * Get property by ID
   */
  async getPropertyById(id) {
    try {
      const response = await axios.get(`/properties/${id}`)
      return {
        data: response.data,
        error: null
      }
    } catch (error) {
      console.error(`Error fetching property ${id}:`, error)
      return {
        data: null,
        error: error.response?.data?.message || 'Failed to fetch property'
      }
    }
  },
  /**
 * Search properties with specific search criteria
 * @param {string} criteria - The search criteria (id, type, city, availability)
 * @param {string} query - The search query text
 * @param {number} page - The page number (0-based)
 * @param {number} size - Number of items per page
 * @param {object} additionalFilters - Any additional filters to apply
 * @returns {Promise} - Promise with search results
 */
searchProperties(criteria, query, page = 0, size = 12, additionalFilters = {}) {
  console.log(`Searching properties by ${criteria}: ${query}`);
  
  // Construct search parameters
  const params = {
    page,
    size,
    ...additionalFilters
  };
  
  // Add the search criteria and query to the parameters
  // The backend API expects parameters in the format the search criteria is used as the parameter name
  // For example, ?type=Apartment or ?city=New%20York
  params[criteria] = query;
  
  // Use the search endpoint (could be the same as getProperties or a specialized endpoint)
  return axios.get('/api/properties/search', { params });
},

/**
 * Get property by specific field and value
 * @param {string} field - The property field to filter by (id, type, city, etc.)
 * @param {string} value - The value to search for
 * @returns {Promise} - Promise with search results
 */
getPropertyByField(field, value) {
  console.log(`Getting property by ${field}: ${value}`);
  
  // For direct field lookups (especially useful for ID searches)
  const params = {};
  params[field] = value;
  
  return axios.get('/api/properties', { params });
}
}

export default PropertiesService