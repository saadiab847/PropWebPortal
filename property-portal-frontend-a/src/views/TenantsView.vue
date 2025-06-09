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

<!-- Loading State -->
<v-row>
<v-col>
<v-progress-circular
   indeterminate
   color="primary"
   size="64"
 ></v-progress-circular>

<p>
Loading tenants...

</p>
</v-col>
</v-row>
<!-- Tenants Grid -->
<v-row v-if="!loading && tenants && tenants.length > 0">
  <v-col
    v-for="(tenant, index) in tenants"
    :key="tenant && tenant.id ? tenant.id : index"
    cols="12"
    sm="6"
    md="4"
    lg="3"
  >
    <v-card
      v-if="tenant"
      class="mx-auto tenant-card"
      elevation="2"
      :to="tenant.id ? '/tenants/' + tenant.id : ''"
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
</v-col> </v-row> 

<!-- No Results -->
<v-row>
<v-col>
<v-alert>
No tenants found

</v-alert>
</v-col>
</v-row>

<!-- Pagination -->
<v-row>
1">

<v-col>
<v-pagination
v-model="page"
:length="totalPages"
@input="fetchTenants"
:total-visible="7"
></v-pagination>

<p>
  Showing {{ tenants.length }} of {{ totalElements }} tenants
</p>
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
async fetchTenants() {
  this.loading = true;
  try {
    const params = {
      page: this.page - 1, // API is 0-based
      size: this.pageSize,
      sort: 'ASC'
    };

    if (this.filters.search) {
      params.search = this.filters.search;
    }

    if (this.filters.status && this.filters.status !== 'All') {
      params.status = this.filters.status;
    }

    const response = await TenantService.getTenants(params);
    this.tenants = response.content || [];
    this.totalElements = response.totalElements || 0;
    this.totalPages = response.totalPages || 0;
  } catch (error) {
    this.error = 'Failed to load tenants';
    console.error('Error fetching tenants:', error);
    this.tenants = [];
  } finally {
    this.loading = false;
  }
},

getPropertyInfo(tenant) {
  if (!tenant) return 'No property assigned';
  if (tenant.property) {
    return tenant.property.name || `Property #${tenant.property.id}`;
  }
  return 'No property assigned';
},

formatDate(dateString) {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString();
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
