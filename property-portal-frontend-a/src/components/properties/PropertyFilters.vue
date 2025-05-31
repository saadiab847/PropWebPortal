<template>
<v-card class="mb-4 pa-4"> <v-form @submit.prevent="applyFilters"> <v-row>
<v-col>
<v-text-field
         v-model="localFilters.keyword"
         label="Search"
         prepend-inner-icon="mdi-magnify"
         clearable
         hide-details
       ></v-text-field>

</v-col>
<v-col>
<v-select
         v-model="localFilters.propertyType"
         :items="propertyTypes"
         label="Property Type"
         clearable
         hide-details
       ></v-select>

</v-col>
<v-col>
<v-select
         v-model="localFilters.listingType"
         :items="listingTypes"
         label="For Sale/Rent"
         clearable
         hide-details
       ></v-select>

</v-col>
<v-col>
<v-select
         v-model="localFilters.bedrooms"
         :items="bedroomOptions"
         label="Bedrooms"
         clearable
         hide-details
       ></v-select>

</v-col>
    <v-col cols="12" sm="6" md="3">
      <v-range-slider
        v-model="localFilters.priceRange"
        :min="priceRange.min"
        :max="priceRange.max"
        :step="priceRange.step"
        hide-details
        label="Price Range"
      >
        <template v-slot:prepend>
          ${{ localFilters.priceRange[0].toLocaleString() }}
</template>
<template>
          ${{ localFilters.priceRange[1].toLocaleString() }}
</template>
      </v-range-slider>
    </v-col>
  </v-row>
<v-row>
<v-col>
<v-btn>
        Reset Filters
</v-btn>
<v-btn>
        Apply Filters
</v-btn>
</v-col>
</v-row>
</v-form>
</v-card> </template>
<script>
export default {
name: 'PropertyFilters',
props: {
initialFilters: {
type: Object,
default: () => ({
keyword: '',
propertyType: '',
listingType: '',
bedrooms: '',
priceRange: [0, 2000000]
})
}
},
data() {
return {
localFilters: {
keyword: this.initialFilters.keyword || '',
propertyType: this.initialFilters.propertyType || '',
listingType: this.initialFilters.listingType || '',
bedrooms: this.initialFilters.bedrooms || '',
priceRange: this.initialFilters.priceRange || [0, 2000000]
},
propertyTypes: ['APARTMENT', 'HOUSE', 'TOWNHOUSE', 'CONDO', 'LAND'],
listingTypes: ['SALE', 'RENT'],
bedroomOptions: [1, 2, 3, 4, '5+'],
priceRange: {
min: 0,
max: 2000000,
step: 10000
}
}
},
watch: {
initialFilters: {
handler(newVal) {
// Update local filters when prop changes
this.localFilters = {
keyword: newVal.keyword || '',
propertyType: newVal.propertyType || '',
listingType: newVal.listingType || '',
bedrooms: newVal.bedrooms || '',
priceRange: newVal.priceRange || [0, 2000000]
};
},
deep: true
}
},
methods: {
applyFilters() {
this.$emit('filter-changed', {...this.localFilters})
},
resetFilters() {
this.localFilters = {
keyword: '',
propertyType: '',
listingType: '',
bedrooms: '',
priceRange: [0, 2000000]
}
this.$emit('filter-changed', {...this.localFilters})
}
}
}

</script>
