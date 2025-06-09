<template>
<v-container>
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
    <v-card class="mb-6">
<v-card-title>
<v-icon>
mdi-filter

</v-icon>
        Filter Tenants
</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" sm="4">
<v-text-field
               v-model="filters.search"
               label="Search"
               prepend-icon="mdi-magnify"
               clearable
               hide-details
               dense
             ></v-text-field>

</v-col>
<v-col>
<v-select
               v-model="filters.status"
               label="Status"
               :items="['Active', 'Inactive', 'All']"
               prepend-icon="mdi-account-check"
               clearable
               hide-details
               dense
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

<!-- Tenants Grid - This is the section you specifically asked to add back -->
<v-row>
0">

<v-col>
    <v-card
      class="mx-auto tenant-card"
      elevation="2",
      :to="tenant && tenant.id ? '/tenants/' + tenant.id : ''"
      hover
    >
<v-avatar>
<v-img
           :src="tenant.profileImageUrl || '/img/default-avatar.png'"
           alt="Tenant Avatar"
         ></v-img>

</v-avatar>
<v-card-title>
        {{ tenant.firstName || '' }} {{ tenant.lastName || '' }}
</v-card-title>
<v-card-subtitle>
        {{ tenant.email || 'No email provided' }}
</v-card-subtitle>
      <v-card-text>
        <v-row dense>
          <v-col cols="12">
<v-icon>
mdi-phone

</v-icon>
            {{ tenant.phone || 'No phone provided' }}
</v-col>
<v-col>
<v-icon>
mdi-home

</v-icon>
            {{ getPropertyInfo(tenant) }}
</v-col>
<v-col>
<v-icon>
mdi-calendar

</v-icon>
            Since: {{ formatDate(tenant.leaseStartDate) }}
</v-col>
</v-row>
      </v-card-text>
<v-card-actions>
<v-chip>
          {{ tenant.status || 'Unknown' }}
</v-chip>
</v-card-actions>
    </v-card>
  </v-col>
</v-row>

<!-- Loading State - Keep simple for now -->
<div>
<v-progress-circular
     indeterminate
     color="primary"
     size="64"
   ></v-progress-circular>

<p>
Loading tenants...

</p>
</div>
<!-- Pagination - Simplified -->
<v-row>
1">

<v-col>
<v-pagination
v-model="page"
:length="totalPages"
@input="fetchTenants"
:total-visible="7"
></v-pagination>

</v-col>
</v-row>
</v-container>
</template>
<script>
import TenantService from '@/services/TenantService';

export default {
name: 'TenantsView',

data() {
return {
tenants: [],
loading: false,
error: null,
page: 1,
pageSize: 12,
totalPages: 0,
totalElements: 0,
filters: {
search: '',
status: 'All'
}
};
},

created() {
this.fetchTenants();
},

methods: {
// Simplified fetch method
async fetchTenants() {
console.log("Fetching tenants...");
this.loading = true;

  try {
    const params = {
      page: this.page - 1,
      size: this.pageSize,
      sort: 'ASC'
    };

    if (this.filters.search) {
      params.search = this.filters.search;
    }

    if (this.filters.status && this.filters.status !== 'All') {
      params.status = this.filters.status;
    }

    console.log("Calling API with params:", params);
    const response = await TenantService.getTenants(params);
    console.log("API response:", response);
    
    this.tenants = response.content.content || [];
    this.totalElements = response.totalElements || 0;
    this.totalPages = response.totalPages || 0;
    
    console.log("Tenants loaded:", this.tenants.length);
  } catch (error) {
    console.error('Error fetching tenants:', error);
    this.error = 'Failed to load tenants';
    this.tenants = [];
  } finally {
    this.loading = false;
    console.log("Loading finished, state:", this.loading);
  }
},

getPropertyInfo(tenant) {
  if (!tenant) return 'No property assigned';
  if (tenant.property) {
    return tenant.property.name || `Property #${tenant.property.id || 'Unknown'}`;
  }
  return 'No property assigned';
},

formatDate(dateString) {
  if (!dateString) return 'N/A';
  try {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  } catch (e) {
    return 'Invalid Date';
  }
}
}
};

</script>
<style>
.tenant-card {
transition: transform 0.3s;
}
.tenant-card:hover {
transform: translateY(-5px);
}

</style>
