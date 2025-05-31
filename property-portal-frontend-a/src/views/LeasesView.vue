<template>
<div class="leases-container"> <v-container>
<h1>
Lease Management

</h1>
  <!-- Filters and Actions -->
<v-card>
<v-row>
<v-col>
<v-text-field
           v-model="searchQuery"
           label="Search Leases"
           prepend-inner-icon="mdi-magnify"
           clearable
           hide-details
           density="compact"
         ></v-text-field>

</v-col>
<v-col>
<v-select
           v-model="statusFilter"
           :items="statusOptions"
           label="Status"
           clearable
           hide-details
           density="compact"
         ></v-select>

</v-col>
<v-col>
<v-select
           v-model="propertyFilter"
           :items="propertyOptions"
           label="Property"
           clearable
           hide-details
           density="compact"
         ></v-select>

</v-col>
<v-col>
<v-btn>
          Add New Lease
</v-btn>
</v-col>
</v-row>
</v-card>
  <!-- Leases Table -->
  <v-card>
    <v-data-table
      :headers="headers"
      :items="leases"
      :loading="loading"
      :items-per-page="10"
      :items-per-page-options="[5, 10, 25, 50]"
      class="elevation-1"
    >
      <template v-slot:item.status="{ item }">
<v-chip>
          {{ item.status }}
</v-chip>
</template>
<template>
        {{ formatDate(item.startDate) }}
</template>
<template>
        {{ formatDate(item.endDate) }}
</template>
<template>
<v-icon>
          mdi-pencil
</v-icon>
<v-icon>
          mdi-eye
</v-icon>
</template>
<template>
<div>
<v-icon>
            mdi-file-document-outline
</v-icon>
<p>
No lease agreements found

</p>
<v-btn>
            Refresh
</v-btn>
</div>
</template>
    </v-data-table>
  </v-card>
  
  <!-- Upcoming Renewals Card -->
<h2>
Upcoming Renewals

</h2>
<v-row>
<v-col>
<v-card>
<v-card-title>
{{ renewal.propertyName }}

</v-card-title>
<v-card-subtitle>
<v-icon>
mdi-account

</v-icon>
          {{ renewal.tenantName }}
</v-card-subtitle>
<v-card-text>
<div>
<span>
Expiration Date:

</span>
<span>
{{ formatDate(renewal.endDate) }}

</span>
</div>
<div>
<span>
Monthly Rent:

</span>
<span>
${{ renewal.rent.toLocaleString() }}

</span>
</div>
<div>
<span>
Days Left:

</span>
<span>
{{ renewal.daysLeft }}

</span>
</div>
</v-card-text>
<v-card-actions>
<v-btn>
Renew Lease

</v-btn>
<v-spacer></v-spacer>

<v-btn>
View Details

</v-btn>
</v-card-actions>
</v-card>
</v-col>
</v-row>
</v-container>
</div> </template>
<script>
export default {
name: 'LeasesView',
data() {
return {
loading: false,
searchQuery: '',
statusFilter: '',
propertyFilter: '',
statusOptions: ['Active', 'Pending', 'Expired', 'Terminated'],
propertyOptions: ['Oakwood Apartments', 'Sunset Heights', 'Riverfront Condos', 'Pine Street Houses'],
headers: [
{ title: 'Property', key: 'propertyName' },
{ title: 'Tenant', key: 'tenantName' },
{ title: 'Start Date', key: 'startDate' },
{ title: 'End Date', key: 'endDate' },
{ title: 'Rent', key: 'rent' },
{ title: 'Status', key: 'status' },
{ title: 'Actions', key: 'actions', sortable: false }
],
leases: [
{
id: 1,
propertyName: 'Oakwood Apartments, Unit 101',
tenantName: 'John Smith',
startDate: '2023-01-01',
endDate: '2024-01-01',
rent: 1250,
status: 'Active'
},
{
id: 2,
propertyName: 'Sunset Heights, Unit 305',
tenantName: 'Emily Johnson',
startDate: '2023-03-15',
endDate: '2024-03-15',
rent: 1800,
status: 'Active'
},
{
id: 3,
propertyName: 'Riverfront Condos, Unit 212',
tenantName: 'Michael Brown',
startDate: '2022-07-01',
endDate: '2023-06-30',
rent: 2100,
status: 'Expired'
},
{
id: 4,
propertyName: 'Pine Street Houses, Unit 7B',
tenantName: 'Sarah Williams',
startDate: '2023-05-01',
endDate: '2023-10-31',
rent: 1500,
status: 'Pending'
},
{
id: 5,
propertyName: 'Oakwood Apartments, Unit 205',
tenantName: 'Robert Davis',
startDate: '2022-10-15',
endDate: '2023-10-15',
rent: 1300,
status: 'Terminated'
}
],
upcomingRenewals: [
{
id: 2,
propertyName: 'Sunset Heights, Unit 305',
tenantName: 'Emily Johnson',
endDate: '2024-03-15',
rent: 1800,
daysLeft: 45
},
{
id: 4,
propertyName: 'Pine Street Houses, Unit 7B',
tenantName: 'Sarah Williams',
endDate: '2023-10-31',
rent: 1500,
daysLeft: 12
},
{
id: 5,
propertyName: 'Oakwood Apartments, Unit 205',
tenantName: 'Robert Davis',
endDate: '2023-10-15',
rent: 1300,
daysLeft: 5
}
]
}
},
methods: {
fetchLeases() {
// Simulate API call
this.loading = true
setTimeout(() => {
this.loading = false
}, 1000)
},
formatDate(dateString) {
const options = { year: 'numeric', month: 'short', day: 'numeric' }
return new Date(dateString).toLocaleDateString(undefined, options)
},
getStatusColor(status) {
switch (status) {
case 'Active': return 'success'
case 'Pending': return 'warning'
case 'Expired': return 'error'
case 'Terminated': return 'grey'
default: return 'primary'
}
},
getDaysLeftClass(days) {
if (days <= 7) return 'text-error font-weight-bold'
if (days <= 30) return 'text-warning font-weight-bold'
return 'text-primary'
},
editLease(lease) {
console.log('Edit lease', lease.id)
// Implement edit functionality
},
viewLease(lease) {
console.log('View lease', lease.id)
// Implement view details functionality
}
},
mounted() {
this.fetchLeases()
}
}

</script>
<style>
.leases-container {
min-height: 80vh;
}

</style>