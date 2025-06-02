<template>
<div>
<div v-if="loading" class="d-flex justify-center align-center my-8">
<v-progress-circular indeterminate color="primary"></v-progress-circular>

</div>
<div>
<v-icon>
mdi-home-search

</v-icon>
<h3>
No properties found

</h3>
<p>
Try adjusting your search filters

</p>
</div>
<v-row>
<v-col>
<property-card :property="property"></property-card>

</v-col>
</v-row>
<div>
<v-pagination>
1"
v-model="currentPage"
:length="totalPages"
@update:modelValue="handlePageChange"
>

</v-pagination>
</div>
</div>
</template>
<script>
import PropertyCard from './PropertyCard.vue'

export default {
name: 'PropertyList',
components: {
PropertyCard
},
props: {
properties: {
type: Array,
required: true,
default: () => []
},
loading: {
type: Boolean,
default: false
},
totalPages: {
type: Number,
default: 1
}
},
data() {
return {
currentPage: 1
}
},
computed: {
displayProperties() {
// This ensures the prop is used in computation
return this.properties.map(property => ({
id: property.id,
title: property.title || 'Unnamed Property',
price: property.price || 0,
propertyType: property.propertyType || 'UNKNOWN',
bedrooms: property.bedrooms || 0,
bathrooms: property.bathrooms || 0,
address: property.address || 'No address provided',
imageUrl: property.imageUrl,
listingType: property.listingType || 'SALE'
}));
}
},
watch: {
totalPages: {
handler(newVal) {
if (this.currentPage > newVal && newVal > 0) {
this.currentPage = newVal
}
},
immediate: true
}
},
methods: {
handlePageChange(page) {
this.currentPage = page;
this.$emit('page-changed', page);
}
}
}

</script>