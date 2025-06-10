<template>
<v-container>
<!-- Debug Information -->
<div>
<div>Debug: Tenants Length: {{ tenants ? tenants.length : 0 }}
</div>
<div>
Loading: {{ loading }}

</div>
</div> <!-- Tenant Cards -->
<v-row>
<v-col>
<v-card>
<v-card-title>
      {{ tenant?.name || 'Unnamed Tenant' }}
</v-card-title>
<v-card-text>
<div>
<v-icon>
mdi-map-marker

</v-icon>
        {{ tenant?.address || 'Unknown Location' }}
</div>
<div>
<v-icon>
mdi-home

</v-icon>
        {{ tenant?.propertyName || 'Unassigned' }}
</div>
<div>
<v-icon>
mdi-email

</v-icon>
        {{ tenant?.email || 'No email' }}
</div>
<div>
<v-icon>
mdi-phone

</v-icon>
        {{ tenant?.phone || 'No phone' }}
</div>
<div>
<v-icon>
mdi-calendar

</v-icon>
        {{ formatDate(tenant?.leaseStartDate) }}
</div>
</v-card-text>
<v-divider></v-divider>

<v-card-actions>
<v-chip>
        {{ tenant?.status || 'Unknown' }}
</v-chip>
<v-btn>
        Details
</v-btn>
</v-card-actions>
</v-card>
</v-col>
</v-row>
<!-- Empty State - Show when no tenants and not loading -->
<v-row>
<v-col>
<v-icon>
mdi-account-off

</v-icon>
<h3>
No tenants found

</h3>
<p>
Try changing your search criteria or add a new tenant.

</p>
</v-col>
</v-row>
<!-- Loading State -->
<v-row>
<v-progress-circular indeterminate color="primary"></v-progress-circular>

</v-row>
<!-- Pagination -->
<div>
0" class="text-center py-3">
<div class="text-subtitle-2 text-grey mb-2">
Showing {{ computedPaginationText }}

</div>
<v-pagination>
1"
v-model="currentPage"
:length="totalPages"
:total-visible="7"
@input="handlePageChange"
>

</v-pagination>
</div>
</v-container>
</template>


<script>
import TenantService from '@/services/TenantService';

export default {
  data() {
  return {
    tenantService: new TenantService(),
    tenants: [],
    loading: false,
    currentPage: 1,
    pageSize: 12,
    totalElements: 0,
    totalPages: 0,
    sortBy: "name",
    sortDirection: "ASC",
    debug: process.env.NODE_ENV !== 'production',
    error: null
  };
},

computed: {
  computedPaginationText() {
    const start = ((this.currentPage - 1) * this.pageSize) + 1;
    const end = Math.min(start + this.tenants.length - 1, this.totalElements);
    return `${start} - ${end} of ${this.totalElements} tenants`;
  }
},

methods: {
  async fetchTenants() {
    this.loading = true;
    
    try {
      const response = await this.tenantService.getTenants({
        page: this.currentPage - 1,
        size: this.pageSize,
        sort: this.sortBy,
        direction: this.sortDirection
      });
      
      const responseData = response.data || response;
      console.log('API Response:', responseData);
      
      if (responseData && typeof responseData === 'object') {
        // Standard Spring Data pagination format
        if (Array.isArray(responseData.content)) {
          this.tenants = responseData.content;
          this.totalElements = responseData.totalElements || 0;
          this.totalPages = responseData.totalPages || 0;
        }
        // Direct array response
        else if (Array.isArray(responseData)) {
          this.tenants = responseData;
          this.totalElements = responseData.length;
          this.totalPages = 1;
        }
        // Single tenant object
        else if (responseData.id) {
          this.tenants = [responseData];
          this.totalElements = 1;
          this.totalPages = 1;
        }
        // Unexpected but attempt to handle
        else {
          console.warn('Unexpected API response format:', responseData);
          this.tenants = [];
          this.totalElements = 0;
          this.totalPages = 0;
        }
      } else {
        console.error('Invalid API response:', responseData);
        this.tenants = [];
        this.totalElements = 0;
        this.totalPages = 0;
      }
    } catch (error) {
      console.error('Error fetching tenants:', error);
      this.error = 'Failed to load tenants';
      this.tenants = [];
    } finally {
      this.loading = false;
    }
  },
  
  getTenantStatusColor(status) {
    if (!status) return 'grey';
    
    const statusMap = {
      'active': 'success',
      'inactive': 'grey',
      'pending': 'warning',
      'terminated': 'error'
    };
    
    return statusMap[status.toLowerCase()] || 'blue-grey';
  },
  
  formatDate(date) {
    if (!date) return 'No date';
    return new Date(date).toLocaleDateString();
  },
  
  handlePageChange(page) {
    this.currentPage = page;
    this.fetchTenants();
  }
  }
};
</script>
<style>
.v-card-title {
font-size: 1.1rem;
line-height: 1.4;
height: 56px;
overflow: hidden;
}

.property-card {
transition: transform 0.3s, box-shadow 0.3s;
}

.property-card:hover {
transform: translateY(-5px);
box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2) !important;
}

.pagination-container {
width: 100%;
display: flex;
justify-content: center;
}

.search-select {
z-index: 10;
}

</style>