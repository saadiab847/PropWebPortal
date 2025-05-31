<template>
<div>
<v-btn>
  Test API Connection
</v-btn>
<v-alert>
  {{ result.message }}
</v-alert>
</div>
</template>
<script>
import ApiService from '../services/ApiService'

export default {
name: 'ApiTest',
data() {
return {
loading: false,
result: null
}
},
methods: {
async testApi() {
this.loading = true
this.result = null

  try {
    const response = await ApiService.getProperties({ page: 0, size: 1 })
    this.result = {
      success: true,
      message: 'Successfully connected to API and retrieved properties'
    }
    console.log('API response:', response)
  } catch (error) {
    this.result = {
      success: false,
      message: `API connection failed: ${error.message}`
    }
    console.error('API error:', error)
  } finally {
    this.loading = false
  }
}
}
}

</script>
