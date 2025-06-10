<template>
<v-container>
<!-- Tenant Cards - Only show when we have tenants and not loading -->
<div>
0">

<v-row>
<v-col>
<v-card>
<v-card-title>
<v-avatar>
<v-img 
             :src="tenant?.profileImageUrl || '/assets/default-avatar.png'" 
             alt="Tenant Avatar"
           ></v-img>

</v-avatar>
<span>
{{ tenant?.name || 'Unnamed Tenant' }}

</span>
</v-card-title>
<v-card-text>
<v-list-item>
<v-list-item-subtitle>
<strong>
Email:

</strong>
{{ tenant?.email || 'Not provided' }}

</v-list-item-subtitle>
</v-list-item>
<v-list-item>
<v-list-item-subtitle>
<strong>
Phone:

</strong>
{{ tenant?.phone || 'Not provided' }}

</v-list-item-subtitle>
</v-list-item>
<v-list-item>
<v-list-item-subtitle>
<strong>
Property:

</strong>
{{ getPropertyInfo(tenant) || 'Not assigned' }}

</v-list-item-subtitle>
</v-list-item>
<v-list-item>
<v-list-item-subtitle>
<strong>
Status:

</strong>
<v-chip>
              {{ tenant?.status || 'Unknown' }}
</v-chip>
</v-list-item-subtitle>
</v-list-item>
</v-card-text>
<v-card-actions>
<v-spacer></v-spacer>

<v-btn>
          View Details
</v-btn>
</v-card-actions>
</v-card>
</v-col>
</v-row>
<!-- Pagination - Only show when we have multiple pages -->
<div v-if="totalPages > 1" class="text-center mt-4">
<v-pagination
v-model="currentPage"
:length="totalPages"
:total-visible="7"
@input="handlePageChange"
></v-pagination>

</div>
</div> <!-- Empty State - Show when no tenants and not loading -->
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
<v-btn>
<v-icon>
mdi-plus

</v-icon>
    Add Tenant
</v-btn>
</v-col>
</v-row>
<!-- Loading Indicator - Show only when loading -->
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
</v-container>
</template>
<script>


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
  // Get a formatted string for property display
  getPropertyInfo(tenant) {
    if (!tenant) return null;
    
    // Check for different property field variations
    const propertyName = tenant.propertyName || 
      (tenant.property ? tenant.property.name : null) ||
      (tenant.lease ? tenant.lease.propertyName : null);
      
    if (!propertyName) return null;
    return propertyName;
  },
  
  // Get appropriate color for tenant status badge
  getTenantStatusColor(status) {
    if (!status) return 'grey';
    
    const statusMap = {
      active: 'success',
      inactive: 'grey',
      pending: 'warning',
      terminated: 'error',
      approved: 'success',
      rejected: 'error',
      review: 'info'
    };
    
    return statusMap[status.toLowerCase()] || 'blue-grey';
  },
  
  // Handle page change in pagination
  handlePageChange(page) {
    this.currentPage = page;
    this.fetchTenants();
  },
  
  // Fetch tenants with the improved response handling
  async fetchTenants() {
    this.loading = true;
    
    try {
      const response = await this.tenantService.fetchTenants({
        page: this.currentPage - 1,  // API uses 0-based indexing
        size: this.pageSize,
        sort: this.sortBy,
        direction: this.sortDirection,
        search: this.searchQuery
      });
      
      // Get the response data, handling both axios responses and direct response objects
      const responseData = response.data || response;
      console.log('Raw API response:', responseData);
      
      if (responseData && typeof responseData === 'object') {
        // Case 1: Spring Data paginated response
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
        else if (responseData.content && typeof responseData.content === 'object') {
          const extractedContent = responseData.content.content || [];
          this.tenants = Array.isArray(extractedContent) ? extractedContent : [];
          this.totalElements = responseData.totalElements || 0;
          this.totalPages = responseData.totalPages || 0;
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
      console.error('Error fetching tenants:', error);
      this.error = 'Failed to load tenants. Please try again later.';
      this.tenants = [];
      this.totalElements = 0;
      this.totalPages = 0;
    } finally {
      this.loading = false;
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
