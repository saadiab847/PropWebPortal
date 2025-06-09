<!-- src/views/TenantsView.vue -->
<template>
<v-container fluid>
<v-row>
<v-col>
<h1>
Tenant Management

</h1>
</v-col>
</v-row>
<!-- Filters Section -->
<v-row>
<v-col>
    <v-card class="mb-4">
<v-card-title>
Filters

</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="4">
<v-text-field
               v-model="filters.search"
               label="Search Tenants"
               prepend-inner-icon="mdi-magnify"
               clearable
             ></v-text-field>

</v-col>
<v-col>
<v-select
               v-model="filters.status"
               label="Status"
               :items="['Active', 'Inactive', 'All']"
               clearable
             ></v-select>

</v-col>
<v-col>
<v-btn>
              Apply Filters
</v-btn>
</v-col>
</v-row>
      </v-card-text>
    </v-card>
  </v-col>
</v-row>

<!-- Tenants Grid -->
<v-row>
  <v-col cols="12">
    <v-card>
      <v-data-table
        :headers="headers"
        :items="tenants"
        :loading="loading"
        :items-per-page="itemsPerPage"
        :server-items-length="totalTenants"
        :page.sync="page"
        @update:page="fetchTenants"
        @update:sort-by="updateSort"
        class="elevation-1"
      >
        <!-- Loading overlay -->
        <template v-slot:progress>
<v-progress-linear indeterminate color="primary"></v-progress-linear>

</template>
        <!-- Actions column -->
<template>
<v-btn>
<v-icon>
mdi-eye

</v-icon>
</v-btn>
<v-btn>
<v-icon>
mdi-pencil

</v-icon>
</v-btn>
</template>
      </v-data-table>
    </v-card>
  </v-col>
</v-row>

<!-- Add Tenant FAB -->
<v-btn>
<v-icon>
mdi-plus

</v-icon>
</v-btn>
</v-container> </template>
<script>
import TenantService from '@/services/TenantService';

export default {
name: 'TenantsView',
data() {
return {
tenants: [],
loading: false,
filters: {
search: '',
status: 'All'
},
headers: [
{ text: 'Name', value: 'name', sortable: true },
{ text: 'Email', value: 'email', sortable: true },
{ text: 'Phone', value: 'phone', sortable: false },
{ text: 'Lease Status', value: 'status', sortable: true },
{ text: 'Actions', value: 'actions', sortable: false }
],
page: 1,
itemsPerPage: 10,
totalTenants: 0,
sortBy: 'name',
sortDesc: false,
showAddTenantDialog: false
};
},
created() {
this.fetchTenants();
},
methods: {
fetchTenants() {
this.loading = true;
const params = {
page: this.page - 1, // API uses 0-based indexing
size: this.itemsPerPage,
sort: ${this.sortBy},${this.sortDesc ? 'desc' : 'asc'},
search: this.filters.search || undefined,
status: this.filters.status !== 'All' ? this.filters.status : undefined
};

  TenantService.getTenants(params)
    .then(response => {
      this.tenants = response.content;
      this.totalTenants = response.totalElements;
      this.loading = false;
    })
    .catch(error => {
      console.error('Error fetching tenants:', error);
      this.loading = false;
      // TODO: Add error handling UI
    });
},
updateSort(sortBy) {
  this.sortBy = sortBy.length > 0 ? sortBy[0] : 'name';
  this.sortDesc = sortBy.length > 0 ? sortBy[1] : false;
  this.fetchTenants();
},
viewTenant(tenant) {
  this.$router.push(`/tenants/${tenant.id}`);
},
editTenant(tenant) {
  this.$router.push(`/tenants/${tenant.id}/edit`);
}
}
};

</script>
