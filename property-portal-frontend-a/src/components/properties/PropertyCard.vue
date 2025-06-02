<template>
<v-card>
<v-img
   :src="property.imageUrl || 'default-property.jpg'"
   height="200"
   cover
 ></v-img>

<v-card-title>
{{ property.title }}

</v-card-title>
<v-card-subtitle>
  ${{ property.price.toLocaleString() }}
<span>
/ month

</span>
</v-card-subtitle>
<v-card-text>
<div>
<v-icon>
mdi-home

</v-icon>
<span>
{{ property.propertyType }}

</span>
<v-divider vertical class="mx-2"></v-divider>

<v-icon>
mdi-bed

</v-icon>
<span>
{{ property.bedrooms }}

</span>
<v-divider vertical class="mx-2"></v-divider>

<v-icon>
mdi-shower

</v-icon>
<span>
{{ property.bathrooms }}

</span>
</div>
<div>
{{ property.address }}

</div>
</v-card-text>
<v-card-actions>
<v-btn>
View Details

</v-btn>
</v-card-actions>
</v-card>
</template>
<script>
export default {
name: 'PropertyCard',
props: {
property: {
type: Object,
required: true,
// Add validator to ensure the property object has required fields
validator: function(obj) {
return obj &&
typeof obj.id !== 'undefined' &&
typeof obj.title !== 'undefined' &&
typeof obj.price !== 'undefined';
}
}
}
}

</script>
<style>
.property-card {
transition: transform 0.3s;
}
.property-card:hover {
transform: translateY(-5px);
box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

</style>
