<template>
<v-app>
<app-navigation />
<v-main>
  <router-view/>
</v-main>
<v-footer>
<div>
    &copy; {{ currentYear }} — Property Portal
</div>
</v-footer>
</v-app>
</template>
<script>
import AppNavigation from './components/AppNavigation.vue'
import { computed } from 'vue'

export default {
name: 'App',
components: {
AppNavigation
},
setup() {
const currentYear = computed(() => new Date().getFullYear())

return {
  currentYear
}
}
}

</script>
<style>
/* Global styles */

</style>
