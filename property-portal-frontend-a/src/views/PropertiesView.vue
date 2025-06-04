<template>
<v-container>
<v-row>
<v-col>
<h1>
Available Properties

</h1>
</v-col>
<v-col>
<div>
      <!-- Search dropdown -->
<v-select v-model="searchCriteria" :items="searchOptions"></v-select>

    </div>
</v-col>
</v-row>
<v-row>
<v-col>
    <!-- Debug info - can be removed in production -->
<v-card>
<div>
<strong>
Debug:

</strong>
Properties Length: {{ properties ? properties.length : 'null' }}

</div>
<div>
<strong>
Loading:

</strong>
{{ loading }}

</div>
</v-card>
    <!-- Search input field - appears when search criteria is selected -->
    <v-card class="mb-4" v-if="searchCriteria !== 'none'">
      <v-card-text>
        <v-row>
          <v-col cols="12" md="8">
<v-text-field
v-model="searchQuery"
:label="`Search ${searchCriteria !== 'none' ? 'by ' + searchCriteria : ''}`"
variant="outlined"
density="compact"
hide-details
class="mr-3"
style="max-inline-size: 250px;"
append-inner-icon="mdi-magnify"
@keyup.enter="applySearch"
:disabled="searchCriteria === 'none'">
</v-text-field>
</v-col>
</v-row>
</v-card-text>
</v-card>

</v-col>
<v-col>
<v-btn @click="applySearch" color="primary" >
Search
</v-btn>
<v-btn @click="clearSearch">
Clear
</v-btn>
</v-col>
</v-row>

    <v-card>
    
    <!-- Loading indicator -->
<v-progress-linear
        v-if="loading"
        indeterminate
        color="primary"
        class="mb-4"
     ></v-progress-linear>

</v-card>
<v-row>
<v-col>

<v-row>
<v-col>
<v-row v-if="!loading && properties && properties.length > 0">
  <v-col v-for="(property, index) in properties" :key="index" cols="12" sm="6" md="4" lg="3">
<v-card>
          <!-- Property image -->
<v-img
             :src="property.imageUrl"
             height="200px"
             cover
           ></v-img>

<v-card-title>
            {{ property.title }}
</v-card-title>
<v-card-text>
<div>
<v-icon>
mdi-map-marker

</v-icon>
<span>
{{ property.location }}

</span>
</div>
<div>
<v-icon>
mdi-home

</v-icon>
<span>
{{ property.propertyType }}

</span>
</div>
<div>
              <div class="d-flex align-center">
<v-icon>
mdi-bed

</v-icon>
<span>
{{ property.bedrooms }}

</span>
</div>
<div>
<v-icon>
mdi-shower

</v-icon>
<span>
{{ property.bathrooms }}

</span>
</div>
<div>
<v-icon>
mdi-ruler-square

</v-icon>
<span>
{{ property.area }} ft²

</span>
</div>
            </div>
</v-card-text>
<v-divider></v-divider>

<v-card-actions>
<v-spacer></v-spacer>

<div>
${{ property.price.toLocaleString() }}

</div>
</v-card-actions>
</v-card>
</v-col>
</v-row>

<div>
<v-card class="d-flex flex-column align-center pa-4">
<div class="text-subtitle-1 mb-2">
Showing {{ (currentPage - 1) * pageSize + 1 }} -
{{ Math.min(currentPage * pageSize, totalProperties) }}
of {{ totalProperties }} properties

</div>
<v-pagination
v-model="currentPage"
:length="totalPages"
:total-visible="7"
@update:modelValue="changePage"
rounded
elevation="2"
></v-pagination>

</v-card>
</div>

</v-col>
</v-row>
</v-col>
</v-row>
</v-container>
</template>


<script>
import PropertyService from '@/services/PropertyService';

export default {
name: 'PropertiesView',

data() {
return {
debug: false, // Set to false for production
loading: false,
properties: [],
totalProperties: 0,
currentPage: 1,
pageSize: 12,
totalPages: 0,

  // New search related data
  searchCriteria: 'none',
  searchQuery: '',
  searchOptions: [
    { title: 'None', value: 'none' },
    { title: 'ID', value: 'id' },
    { title: 'Type', value: 'type' },
    { title: 'City', value: 'city' },
    { title: 'Availability', value: 'availability' }
  ],
  
  // Original filters
  filters: {
    location: '',
    propertyType: null,
    minPrice: null,
    maxPrice: null
  },
  propertyTypes: [
    'Apartment',
    'House',
    'Condo',
    'Townhouse',
    'Villa',
    'Commercial'
  ]
};
},

created() {
this.searchCriteria = 'none'; // Explicitly set default value
this.loadProperties();
},

methods: {
loadProperties() {
this.loading = true;
console.log('Loading properties...');

  // API uses 0-based indexing for pages
  const page = this.currentPage - 1;
  
  // Add search criteria to filters if applicable
  const searchFilters = { ...this.filters };
  if (this.searchCriteria !== 'none' && this.searchQuery) {
    searchFilters[this.searchCriteria] = this.searchQuery;
  }
  
  PropertyService.getProperties(page, this.pageSize, searchFilters)
    .then(response => {
      if (response && response.data) {
        // Handle different API response structures
        if (Array.isArray(response.data)) {
          // Direct array response
          this.processProperties(response.data);
          this.totalProperties = response.data.length;
          this.totalPages = 1;
        } 
        else if (response.data.content) {
          // Paginated response with content property
          this.processProperties(response.data.content);
          this.totalProperties = response.data.totalElements || 0;
          this.totalPages = response.data.totalPages || 0;
        } 
        else {
          // Flat object structure with no content property
          this.processProperties([response.data]); // Wrap single object in array
          this.totalProperties = 1;
          this.totalPages = 1;
        }
      } else {
        console.error('Unexpected API response format');
        this.properties = [];
        this.totalProperties = 0;
        this.totalPages = 0;
      }
    })
    .catch(error => {
      console.error('Error loading properties:', error);
      this.properties = [];
      this.totalProperties = 0;
      this.totalPages = 0;
    })
    .finally(() => {
      this.loading = false;
    });
},

// Process properties with uniform structure
processProperties(data) {
  // Map each property to ensure consistent structure
  this.properties = data.map(p => ({
    id: p.id || 0,
    title: p.title || 'Untitled Property',
    location: p.location || 'Unknown Location',
    price: p.price || 0,
    imageUrl: p.imageUrl || 'https://via.placeholder.com/300x200?text=Property+Image',
    propertyType: p.propertyType || 'Unknown Type',
    bedrooms: p.bedrooms || 0,
    bathrooms: p.bathrooms || 0,
    area: p.area || 0
  }));
},

changePage(page) {
  this.currentPage = page;
  // Scroll to top when changing pages
  window.scrollTo({ top: 0, behavior: 'smooth' });
  this.loadProperties();
},

// New search methods
// Update the applySearch method in PropertiesView.vue
applySearch() {
  if (this.searchCriteria === 'none' || !this.searchQuery.trim()) {
    return;
  }
  
  this.loading = true;
  this.currentPage = 1; // Reset to first page when searching
  
  // Use the dedicated search method
  PropertyService.searchProperties(
    this.searchCriteria,
    this.searchQuery,
    this.currentPage - 1,
    this.pageSize,
    this.filters
  )
  .then(response => {
    if (response && response.data) {
      // Handle different API response structures
      if (Array.isArray(response.data)) {
        // Direct array response
        this.processProperties(response.data);
        this.totalProperties = response.data.length;
        this.totalPages = 1;
      } 
      else if (response.data.content) {
        // Paginated response with content property
        this.processProperties(response.data.content);
        this.totalProperties = response.data.totalElements || 0;
        this.totalPages = response.data.totalPages || 0;
      } 
      else {
        // Flat object structure with no content property
        this.processProperties([response.data]); // Wrap single object in array
        this.totalProperties = 1;
        this.totalPages = 1;
      }
    } else {
      console.error('Unexpected API response format');
      this.properties = [];
      this.totalProperties = 0;
      this.totalPages = 0;
    }
  })
  .catch(error => {
    console.error('Error searching properties:', error);
    this.properties = [];
    this.totalProperties = 0;
    this.totalPages = 0;
  })
  .finally(() => {
    this.loading = false;
  });
},
clearSearch() {
  this.searchQuery = '';
  this.currentPage = 1;
  this.loadProperties();
},

// Original filter methods
resetFilters() {
  this.filters = {
    location: '',
    propertyType: null,
    minPrice: null,
    maxPrice: null
  };
  this.searchCriteria = 'none';
  this.searchQuery = '';
  this.currentPage = 1;
  this.loadProperties();
},

applyFilters() {
  this.currentPage = 1; // Reset to first page when applying filters
  this.loadProperties();
}
}
};

</script>

<style>
.v-card-title {
font-size: 1.1rem;
line-height: 1.4;
height: 56px;
overflow: hidden;
}

.property-card {
transition: transform 0.3s, box-shadow 0.3s;
}

.property-card:hover {
transform: translateY(-5px);
box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2) !important;
}

.pagination-container {
width: 100%;
display: flex;
justify-content: center;
}

.search-select {
z-index: 10;
}

</style>