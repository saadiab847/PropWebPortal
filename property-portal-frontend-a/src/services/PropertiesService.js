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
  }
}

export default PropertiesService