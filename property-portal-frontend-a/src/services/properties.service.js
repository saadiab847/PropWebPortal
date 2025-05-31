// src/services/properties.service.js
import apiClient from './api.service';

const PropertiesService = {
  /**
   * Get properties with pagination, sorting, and filtering
   * @param {Object} params - Query parameters
   * @returns {Promise} - Axios promise
   */
  getProperties(params = {}) {
    return apiClient.get('/properties', { params });
  },

  /**
   * Get a single property by ID
   * @param {string|number} id - Property ID
   * @returns {Promise} - Axios promise
   */
  getProperty(id) {
    return apiClient.get(`/properties/${id}`);
  },

  /**
   * Create a new property
   * @param {Object} property - Property data
   * @returns {Promise} - Axios promise
   */
  createProperty(property) {
    return apiClient.post('/properties', property);
  },

  /**
   * Update an existing property
   * @param {string|number} id - Property ID
   * @param {Object} property - Updated property data
   * @returns {Promise} - Axios promise
   */
  updateProperty(id, property) {
    return apiClient.put(`/properties/${id}`, property);
  },

  /**
   * Delete a property
   * @param {string|number} id - Property ID
   * @returns {Promise} - Axios promise
   */
  deleteProperty(id) {
    return apiClient.delete(`/properties/${id}`);
  },

  /**
   * Transform API response to match expected property model
   * @param {Object} apiProperty - Property data from API
   * @returns {Object} - Transformed property object
   */
  transformProperty(apiProperty) {
    return {
      id: apiProperty.id,
      title: apiProperty.title || apiProperty.name || 'Unnamed Property',
      price: apiProperty.price || 0,
      propertyType: apiProperty.propertyType || 'UNKNOWN',
      bedrooms: apiProperty.bedrooms || 0,
      bathrooms: apiProperty.bathrooms || 0,
      address: apiProperty.address || 'No address provided',
      imageUrl: apiProperty.imageUrl || apiProperty.image,
      listingType: apiProperty.listingType || 'SALE',
      // Add any additional transformations needed
      area: apiProperty.area || 0,
      description: apiProperty.description || '',
      createdAt: apiProperty.createdAt,
      updatedAt: apiProperty.updatedAt
    };
  }
};

export default PropertiesService;