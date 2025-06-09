import axios from 'axios';

const API_URL = process.env.VUE_APP_API_URL || 'http://localhost:8080/api';

export default {
  /**
   * Get all tenants with optional pagination, sorting, and filtering
   * @param {Object} params - Query parameters
   * @param {Number} params.page - Page number (0-based)
   * @param {Number} params.size - Page size
   * @param {String} params.sort - Sort field and direction (e.g. 'name,asc')
   * @param {String} params.search - Search term
   * @returns {Promise} - Promise containing tenant data
   */
  getTenants(params = {}) {
    return axios.get(`${API_URL}/tenants`, { params })
      .then(response => {
        return this.processTenants(response.data);
      })
      .catch(error => {
        console.error('Error fetching tenants:', error);
        throw error;
      });
  },

  /**
   * Process tenant data from various response formats
   * @param {Object|Array} data - API response data
   * @returns {Object} - Processed tenant data with consistent format
   */
  processTenants(data) {
    // Handle different response formats
    if (Array.isArray(data)) {
      // Direct array of tenants
      return {
        content: data,
        totalElements: data.length,
        totalPages: 1,
        size: data.length,
        number: 0
      };
    } else if (data.content && Array.isArray(data.content)) {
      // Paginated response
      return data;
    } else {
      // Single object response or other format
      return {
        content: [data],
        totalElements: 1,
        totalPages: 1,
        size: 1,
        number: 0
      };
    }
  },

  /**
   * Get a specific tenant by ID
   * @param {String|Number} id - Tenant ID
   * @returns {Promise} - Promise containing tenant data
   */
  getTenantById(id) {
    return axios.get(`${API_URL}/tenants/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error(`Error fetching tenant with ID ${id}:`, error);
        throw error;
      });
  },

  /**
   * Create a new tenant
   * @param {Object} tenantData - Tenant data to create
   * @returns {Promise} - Promise containing created tenant
   */
  createTenant(tenantData) {
    return axios.post(`${API_URL}/tenants`, tenantData)
      .then(response => response.data)
      .catch(error => {
        console.error('Error creating tenant:', error);
        throw error;
      });
  },

  /**
   * Update an existing tenant
   * @param {String|Number} id - Tenant ID
   * @param {Object} tenantData - Updated tenant data
   * @returns {Promise} - Promise containing updated tenant
   */
  updateTenant(id, tenantData) {
    return axios.put(`${API_URL}/tenants/${id}`, tenantData)
      .then(response => response.data)
      .catch(error => {
        console.error(`Error updating tenant with ID ${id}:`, error);
        throw error;
      });
  },

  /**
   * Delete a tenant by ID
   * @param {String|Number} id - Tenant ID to delete
   * @returns {Promise} - Promise containing result
   */
  deleteTenant(id) {
    return axios.delete(`${API_URL}/tenants/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error(`Error deleting tenant with ID ${id}:`, error);
        throw error;
      });
  }
};