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
@update:modelValue="$emit('page-changed', $event)"
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
required: true
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
watch: {
totalPages: {
handler(newVal) {
if (this.currentPage > newVal && newVal > 0) {
this.currentPage = newVal
}
},
immediate: true
}
}
}

</script>