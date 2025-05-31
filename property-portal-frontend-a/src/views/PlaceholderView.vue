<template>
<v-container>
<v-icon>
{{ getFeatureIcon() }}

</v-icon>
<h1>
{{ $route.meta.featureName }}

</h1>
<p>
  This feature is coming soon
</p>
<v-btn>
  Return to Dashboard
</v-btn>
</v-container>
</template>
<script>
export default {
name: 'PlaceholderView',
methods: {
getFeatureIcon() {
const route = this.$route.path
if (route.includes('tenants')) return 'mdi-account-group'
if (route.includes('entities')) return 'mdi-domain'
if (route.includes('apartments')) return 'mdi-door'
if (route.includes('maintenance')) return 'mdi-tools'
if (route.includes('payments')) return 'mdi-cash-multiple'
if (route.includes('documents')) return 'mdi-file-document-multiple'
if (route.includes('reports')) return 'mdi-chart-box'
if (route.includes('settings')) return 'mdi-cog'
return 'mdi-star'
}
}
}

</script>