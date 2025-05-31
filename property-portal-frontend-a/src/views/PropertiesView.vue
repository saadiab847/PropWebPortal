<template>
<div>
<v-container>
<h1>
Available Properties

</h1>
<property-filters
@filter-changed="updateFilters"
:initial-filters="filters"
></property-filters>

  <v-card class="view-toggle-card pa-2 mb-4">
    <div class="d-flex justify-space-between align-center">
      <div>
<span>
          {{ totalProperties }} {{ totalProperties === 1 ? 'property' : 'properties' }} found
</span>
<span>
          Searching properties...
</span>
</div>
<div>
<v-btn-toggle>
<v-btn>
            Newest
</v-btn>
<v-btn>
            Price ↑
</v-btn>
<v-btn>
            Price ↓
</v-btn>
</v-btn-toggle>
</div>
    </div>
  </v-card>
  
  <!-- Error alert -->
<v-alert>
    {{ error }}
</v-alert>
<property-list
:properties="properties"
:loading="loading"
:total-pages="totalPages"
@page-changed="changePage"
></property-list>

</v-container>
</div>
</template>
<script>
import PropertyList from '../components/properties/PropertyList.vue'
import PropertyFilters from '../components/properties/PropertyFilters.vue'
import PropertiesService from '../services/PropertiesService'

export default {
name: 'PropertiesView',
components: {
PropertyList,
PropertyFilters
},
data() {
return {
properties: [],
loading: false,
error: null,
totalProperties: 0,
totalPages: 1,
currentPage: 1,
pageSize: 12,
sortBy: 'newest',
filters: {
keyword: '',
propertyType: '',
listingType: '',
bedrooms: '',
priceRange: [0, 2000000]
}
}
},
computed: {
// Prepare sort parameter for API
sortParameter() {
switch (this.sortBy) {
case 'price_asc':
return 'price,asc'
case 'price_desc':
return 'price,desc'
case 'newest':
default:
return 'createdAt,desc'
}
},

// Prepare API query options
queryOptions() {
  const options = {
    page: this.currentPage - 1, // API uses 0-based indexing
    size: this.pageSize,
    sort: this.sortParameter,
  }
  
  // Add filter parameters if they have values
  if (this.filters.keyword) {
    options.search = this.filters.keyword
  }
  
  if (this.filters.propertyType) {
    options.propertyType = this.filters.propertyType
  }
  
  if (this.filters.listingType) {
    options.listingType = this.filters.listingType
  }
  
  if (this.filters.bedrooms) {
    options.bedrooms = this.filters.bedrooms === '5+' ? 5 : this.filters.bedrooms
  }
  
  if (this.filters.priceRange && this.filters.priceRange.length === 2) {
    options.minPrice = this.filters.priceRange[0]
    options.maxPrice = this.filters.priceRange[1]
  }
  
  return options
}
},
mounted() {
// Check for query parameters in URL
this.parseQueryParams()
// Fetch properties
this.fetchProperties()
},
methods: {
// Parse URL query parameters to set initial filters
parseQueryParams() {
const params = new URLSearchParams(window.location.search)

  if (params.get('search')) {
    this.filters.keyword = params.get('search')
  }
  
  if (params.get('type')) {
    this.filters.propertyType = params.get('type')
  }
  
  if (params.get('listing')) {
    this.filters.listingType = params.get('listing')
  }
  
  if (params.get('bedrooms')) {
    this.filters.bedrooms = params.get('bedrooms')
  }
  
  if (params.get('page')) {
    this.currentPage = parseInt(params.get('page')) || 1
  }
  
  if (params.get('sort')) {
    this.sortBy = params.get('sort')
  }
},

// Update browser URL with current filters
updateQueryParams() {
  const params = new URLSearchParams()
  
  if (this.filters.keyword) {
    params.set('search', this.filters.keyword)
  }
  
  if (this.filters.propertyType) {
    params.set('type', this.filters.propertyType)
  }
  
  if (this.filters.listingType) {
    params.set('listing', this.filters.listingType)
  }
  
  if (this.filters.bedrooms) {
    params.set('bedrooms', this.filters.bedrooms)
  }
  
  if (this.currentPage > 1) {
    params.set('page', this.currentPage.toString())
  }
  
  if (this.sortBy !== 'newest') {
    params.set('sort', this.sortBy)
  }
  
  const queryString = params.toString()
  const url = queryString 
    ? `${window.location.pathname}?${queryString}` 
    : window.location.pathname
    
  window.history.replaceState({}, '', url)
},

// Update filters from filter component
updateFilters(newFilters) {
  this.filters = newFilters
  this.currentPage = 1
  this.fetchProperties()
},

// Change page
changePage(page) {
  this.currentPage = page
  this.fetchProperties()
},

// Fetch properties from backend
async fetchProperties() {
  this.loading = true
  this.error = null
  
  try {
    // Update URL query parameters
    this.updateQueryParams()
    
    // Call API service
    const result = await PropertiesService.getProperties(this.queryOptions)
    
    if (result.error) {
      this.error = result.error
      return
    }
    
    // Update component data with response
    const responseData = result.data
    this.properties = responseData.content || []
    this.totalProperties = responseData.totalElements || 0
    this.totalPages = responseData.totalPages || 1
    
    // If current page is greater than total pages, go to last page
    if (this.currentPage > this.totalPages && this.totalPages > 0) {
      this.currentPage = this.totalPages
      this.fetchProperties()
    }
  } catch (error) {
    console.error('Error in fetchProperties:', error)
    this.error = 'An unexpected error occurred while fetching properties'
    this.properties = []
    this.totalProperties = 0
    this.totalPages = 1
  } finally {
    this.loading = false
  }
}
},
watch: {
// Watch sort selection for changes
sortBy() {
this.fetchProperties()
}
}
}

</script>
<style>
.properties-container {
min-block-size: 80vh;
}
.view-toggle-card {
background-color: #f9f9f9;
}

</style>
