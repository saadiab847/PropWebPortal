<template>
<v-container>
<v-row>
<v-col>
<h1>
Available Properties

</h1>
    <!-- Search filters -->
    <v-card class="mb-6">
<v-card-title>
Filters

</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" sm="6" md="3">
<v-text-field
v-model="filters.location"
label="Location"
outlined
dense
@keyup.enter="loadProperties"
></v-text-field>

</v-col>
<v-col>
<v-select
v-model="filters.propertyType"
:items="propertyTypes"
label="Property Type"
outlined
dense
clearable
@change="loadProperties"
></v-select>

</v-col>
<v-col>
<v-text-field
v-model="filters.minPrice"
label="Min Price"
type="number"
outlined
dense
@keyup.enter="loadProperties"
></v-text-field>

</v-col>
<v-col>
<v-text-field
v-model="filters.maxPrice"
label="Max Price"
type="number"
outlined
dense
@keyup.enter="loadProperties"
></v-text-field>

</v-col>
</v-row>
<v-btn>
Apply Filters

</v-btn>
<v-btn>
Reset

</v-btn>
      </v-card-text>
    </v-card>
    
    <!-- Loading indicator -->
<v-progress-linear
       v-if="loading"
       indeterminate
       color="primary"
       class="mb-4"
     ></v-progress-linear>

    <!-- Property listings grid -->
<v-row>
<v-col>
<v-card>
          <!-- Property image -->
<v-img
             :src="property.imageUrl || 'https://via.placeholder.com/300x200?text=Property+Image'"
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
              <div>
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
    <!-- Empty state -->
<v-card>
<v-icon>
mdi-home-search

</v-icon>
<h3>
No properties found

</h3>
<p>
Try adjusting your filters or check back later for new listings

</p>
</v-card>
    <!-- Pagination -->
<div>
<v-pagination>
1"
v-model="currentPage"
:length="totalPages"
:total-visible="7"
@input="changePage"
>

</v-pagination>
</div>
  </v-col>
</v-row>
</v-container>
</template>
<script>
import PropertyService from '@/services/PropertyService';

export default {
name: 'Properties',

data() {
return {
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
// API uses 0-based indexing for pages
const page = this.currentPage - 1;

  PropertyService.getProperties(page, this.pageSize, this.filters)
    .then(response => {
      this.properties = response.data.content;
      this.totalProperties = response.data.totalElements;
      this.totalPages = response.data.totalPages;
    })
    .catch(error => {
      console.error('Error loading properties:', error);
      this.$toast.error('Failed to load properties');
    })
    .finally(() => {
      this.loading = false;
    });
},

changePage(page) {
  this.currentPage = page;
  this.loadProperties();
},

resetFilters() {
  this.filters = {
    location: '',
    propertyType: null,
    minPrice: null,
    maxPrice: null
  };
  this.loadProperties();
}
}
};

</script>