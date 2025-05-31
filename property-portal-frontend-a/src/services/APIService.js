import axios from 'axios'

const ApiService = {
  /**
   * Check if API is reachable
   */
  ping() {
    return axios.get('/ping')
  },
  
  /**
   * Get all properties
   */
  getProperties(params = {}) {
    return axios.get('/properties', { params })
  },
  
  /**
   * Get property by ID
   */
  getProperty(id) {
    return axios.get(`/properties/${id}`)
  }
}

export default ApiService