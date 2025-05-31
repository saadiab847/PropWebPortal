<script>
import PropertiesService from '../services/PropertiesService'

export default {
name: 'HomeView',
data() {
return {
stats: {
properties: 0,
tenants: 0,
owners: 0,
maintenance: 0
},
loading: false,
error: null
}
},
mounted() {
this.fetchDashboardStats()
},
methods: {
async fetchDashboardStats() {
this.loading = true

  try {
    const result = await PropertiesService.getDashboardStats()
    
    if (result.error) {
      this.error = result.error
      console.error('Error fetching dashboard stats:', result.error)
      // Use fallback data if API call fails
      this.stats = {
        properties: 24,
        tenants: 18,
        owners: 12,
        maintenance: 5
      }
      return
    }
    
    this.stats = result.data
  } catch (error) {
    console.error('Failed to fetch dashboard statistics:', error)
    // Fallback with dummy data if API fails
    this.stats = {
      properties: 24,
      tenants: 18,
      owners: 12,
      maintenance: 5
    }
  } finally {
    this.loading = false
  }
}
}
}

</script>