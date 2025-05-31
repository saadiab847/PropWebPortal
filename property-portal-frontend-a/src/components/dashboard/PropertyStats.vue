<template>
<v-card>
<v-icon>
mdi-home-city

</v-icon>
<h2>
{{ count }}

</h2>
<p>
{{ label }}

</p>
</v-card>
</template>
<script>
export default {
name: 'PropertyStats',
props: {
count: {
type: Number,
required: true
},
label: {
type: String,
required: true
}
}
}

</script>