<template>
<v-container>
<v-row>
<v-col>
<h1>
Available Properties

</h1>
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
    <!-- Search filters section - keep as is -->
    
    <!-- Loading indicator -->
<v-progress-linear
       v-if="loading"
       indeterminate
       color="primary"
       class="mb-4"
     ></v-progress-linear>

    <!-- Property listings grid -->
    <v-row v-if="!loading && properties && properties.length > 0">
      <v-col 
        v-for="(property, index) in properties" 
        :key="index" 
        cols="12" sm="6" md="4" lg="3"
      >
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
    <!-- Empty state - FIXED: only show when there are NO properties -->
<!-- <v-card>
<v-icon>
mdi-home-search

</v-icon>
<h3>
No properties found

</h3>
<p>
Try adjusting your filters or check back later for new listings

</p>
</v-card> -->
    <!-- Enhanced pagination -->
<div>
<!-- 0 && totalPages > 1"
class="pagination-container mt-6"
> -->
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
this.loadProperties();
},

methods: {
loadProperties() {
this.loading = true;
console.log('Loading properties...');

  // API uses 0-based indexing for pages
  const page = this.currentPage - 1;
  
  PropertyService.getProperties(page, this.pageSize, this.filters)
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

resetFilters() {
  this.filters = {
    location: '',
    propertyType: null,
    minPrice: null,
    maxPrice: null
  };
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

</style>