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
<div>
0" class="tenant-grid">
<v-container fluid>
<v-row>
<!-- Loop through tenants array -->
<v-col v-for="(tenant, index) in tenants" 
          :key="tenant.id || index" 
          cols="12" sm="6" md="4" lg="3">
<v-card elevation="2" class="tenant-card">

<v-card-title>
        {{ tenant.name || '' }} ({{ tenant.tenantCode || '' }})
</v-card-title>
      <v-card-text>
        <div class="tenant-info">
<p>
<strong>
Email:

</strong>
{{ tenant.email || 'N/A' }}

</p>
<p>
<strong>
Phone:

</strong>
{{ tenant.phone || 'N/A' }}

</p>
<p>
<strong>
Property:

</strong>
{{ getPropertyInfo(tenant) || 'N/A' }}

</p>
<p>
<strong>
Lease Start:

</strong>
{{ formatDate(tenant.leaseStartDate) || 'N/A' }}

</p>
<p>
<strong>
Status:

</strong>
<v-chip>
              {{ tenant.status || 'Unknown' }}
</v-chip>
</p>
</div>
      </v-card-text>
<v-card-actions>
<v-btn>
          View Details
</v-btn>
</v-card-actions>
    </v-card>
  </v-col>
</v-row>
</v-container> </div>

<v-row>
<v-progress-circular indeterminate color="primary" v-show="loading"></v-progress-circular>

</v-row>
<!-- Pagination -->
<div
 class="text-center py-3">
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
    TenantService: TenantService,
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
  // Add this lifecycle hook to fetch data when component mounts
mounted() {
    console.log('TenantsView component mounted, calling fetchTenants');
    this.fetchTenants();
},
// Add watchers to react to parameter changes
watch: {
    // Reload when page changes
    currentPage() {
      console.log('Page changed, calling fetchTenants');
      this.fetchTenants();
    },
    
    // Reload when items per page changes
    itemsPerPage() {
      console.log('Items per page changed, calling fetchTenants');
      this.currentPage = 1; // Reset to first page
      this.fetchTenants();
    },
    
    // Reload when sort parameters change
    sortBy() {
      console.log('Sort field changed, calling fetchTenants');
      this.fetchTenants();
    },
    
    sortDirection() {
      console.log('Sort direction changed, calling fetchTenants');
      this.fetchTenants();
    }
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
  console.log('Starting to fetch tenants with params:', {
    page: this.currentPage - 1, // Converting from 1-based to 0-based
    size: this.itemsPerPage,
    sort: this.sortBy,
    direction: this.sortDirection
  });
  
  try {
    // Fix params to match exactly what the backend expects
    const response = await TenantService.getTenants({
      'page': 0,  // Hardcode to 0 for testing
      'size': 10, // Use a reasonable size for testing
      'sort': 'asc' // Simplify sort parameter
    });
    
    // Log raw response before processing
    console.log('API call returned successfully');
    const responseData = response.data;
    console.log('Raw API response:', responseData);
    if (responseData && typeof responseData === 'object') {
      // Case 1: Spring Data paginated response (standard format from backend)
      // This matches the structure we're seeing in the error: {content: Array(1), totalPages: 1, totalElements: 1, ...}
      if (Array.isArray(responseData.content)) {
        this.tenants = responseData.content;
        this.totalElements = responseData.totalElements || 0;
        this.totalPages = responseData.totalPages || 0;
        console.log('Processed as Spring Data pagination format');
      }
      // Case 2: Direct array response
      else if (Array.isArray(responseData)) {
        this.tenants = responseData;
        this.totalElements = responseData.length;
        this.totalPages = 1;
        console.log('Processed as direct array');
      }
      // Case 3: Nested pagination object with content
      else if (responseData.page && Array.isArray(responseData.page.content)) {
        this.tenants = responseData.page.content;
        this.totalElements = responseData.page.totalElements || 0;
        this.totalPages = responseData.page.totalPages || 0;
        console.log('Processed as nested pagination object');
      }
      // Case 4: Single tenant object
      else if (responseData.id) {
        this.tenants = [responseData];
        this.totalElements = 1;
        this.totalPages = 1;
        console.log('Processed as single tenant object');
      }
      // Case 5: Unexpected format but has some identifiable structure
      else if (responseData.content && typeof responseData.content === 'object' && !Array.isArray(responseData.content)) {
        // Handle nested content object that isn't an array
        const extractedContent = responseData.content.content || [];
        this.tenants = Array.isArray(extractedContent) ? extractedContent : [];
        this.totalElements = responseData.content.totalElements || responseData.totalElements || 0;
        this.totalPages = responseData.content.totalPages || responseData.totalPages || 0;
        console.log('Processed as nested content object');
      }
      // Case 6: Fallback for unexpected formats
      else {
        console.warn('Unrecognized API response format:', responseData);
        this.tenants = [];
        this.totalElements = 0;
        this.totalPages = 0;
      }
      // Ensure tenants is always an array
      if (!Array.isArray(this.tenants)) {
        console.error('Tenants data is not an array after processing:', this.tenants);
        this.tenants = [];
      }
      
      // Debug output
      console.log('Processed tenants array:', this.tenants);
      console.log('Total elements:', this.totalElements);
      console.log('Total pages:', this.totalPages);
    } else {
      console.error('Invalid API response:', responseData);
      this.tenants = [];
      this.totalElements = 0;
      this.totalPages = 0;
    }
 } catch (error) {
    console.error('Error fetching tenants - details:', error);
    if (error.response) {
      console.error('Error response data:', error.response.data);
      console.error('Error response status:', error.response.status);
    } else if (error.request) {
      console.error('No response received from server');
    } else {
      console.error('Error setting up request:', error.message);
    }
    this.tenants = [];
    this.totalElements = 0;
    this.totalPages = 0;
  } finally {
    this.loading = false;
  }
},
 getPropertyInfo(tenant) {
    if (!tenant) return 'N/A';
    if (tenant.property) {
      return tenant.property.name || tenant.property.address || 'Unnamed Property';
    }
    return 'No Property Assigned';
  },
  
  formatDate(dateString) {
    if (!dateString) return 'N/A';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString();
    } catch (e) {
      console.error('Error formatting date:', e);
      return dateString || 'N/A';
    }
  },
  
  getStatusColor(status) {
    if (!status) return 'grey';
    
    const statusColors = {
      'ACTIVE': 'green',
      'PENDING': 'orange',
      'EXPIRED': 'red',
      'TERMINATED': 'red'
    };
    
    return statusColors[status.toUpperCase()] || 'grey';
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