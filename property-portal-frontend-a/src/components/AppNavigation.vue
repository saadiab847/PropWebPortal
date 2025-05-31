<template>
<v-app-bar>
<v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>

<v-toolbar-title>
<router-link>
<v-icon>
mdi-home-city

</v-icon>
    Property Portal
</router-link>
</v-toolbar-title>
<v-spacer></v-spacer>

<v-btn>
<v-icon>
mdi-bell

</v-icon>
</v-btn>
<v-btn>
<v-icon>
mdi-cog

</v-icon>
</v-btn>
<!-- Replace v-menu with manual menu handling -->
<div>
<v-btn>
<v-avatar>
<v-img src="https://randomuser.me/api/portraits/women/85.jpg"></v-img>

</v-avatar>
    Jane Smith
<v-icon>
mdi-chevron-down

</v-icon>
</v-btn>
<v-menu>
<v-list>
<v-list-item>
<v-list-item-title>
My Profile

</v-list-item-title>
</v-list-item>
<v-list-item>
<v-list-item-title>
Settings

</v-list-item-title>
</v-list-item>
<v-divider></v-divider>

<v-list-item>
<v-list-item-title>
Logout

</v-list-item-title>
</v-list-item>
</v-list>
</v-menu>
</div>
</v-app-bar>
<v-navigation-drawer v-model="drawer" temporary> <v-list> <v-list-item to="/"> <template v-slot:prepend>
<v-icon>
mdi-view-dashboard

</v-icon>
</template>
<v-list-item-title>
Dashboard

</v-list-item-title>
  </v-list-item>
<v-list-item>
<template>
<v-icon>
mdi-home-search

</v-icon>
</template>
<v-list-item-title>
Properties

</v-list-item-title>
</v-list-item>
<v-list-item>
<template>
<v-icon>
mdi-account-group

</v-icon>
</template>
<v-list-item-title>
Tenants

</v-list-item-title>
</v-list-item>
<v-list-item>
<template>
<v-icon>
mdi-account-tie

</v-icon>
</template>
<v-list-item-title>
Owners

</v-list-item-title>
</v-list-item>
<v-list-item>
<template>
<v-icon>
mdi-tools

</v-icon>
</template>
<v-list-item-title>
Maintenance

</v-list-item-title>
</v-list-item>
<v-list-item>
<template>
<v-icon>
mdi-file-document

</v-icon>
</template>
<v-list-item-title>
Leases

</v-list-item-title>
</v-list-item>
<v-list-item>
<template>
<v-icon>
mdi-cash-multiple

</v-icon>
</template>
<v-list-item-title>
Payments

</v-list-item-title>
</v-list-item>
</v-list>
</v-navigation-drawer> </template>
<script>
export default {
name: 'AppNavigation',
data() {
return {
drawer: false,
showMenu: false,
menuButton: null
}
},
mounted() {
this.menuButton = this.$refs.menuButton
},
methods: {
logout() {
console.log('Logged out')
this.showMenu = false
// Implement logout logic here
// then navigate to login
}
}
}

</script>