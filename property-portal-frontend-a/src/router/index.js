import { createRouter, createWebHistory } from 'vue-router'
// Route components - you may need to adjust these paths to match your project structure
const HomeView = () => import('../views/HomeView.vue')
const PropertiesView = () => import('../views/PropertiesView.vue')

// Define routes
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: {
      title: 'Home - Property Portal'
    }
  },
  {
    path: '/properties',
    name: 'properties',
    component: PropertiesView,
    meta: {
      title: 'Properties - Property Portal'
    }
  },
  {
    path: '/tenants',
    name: 'tenants',
    component: () => import('../views/TenantsView.vue'),
    meta: {
      title: 'Tenants - Property Portal'
    }
  },
  {
    path: '/owners',
    name: 'owners',
    component: () => import('../views/OwnersView.vue'),
    meta: {
      title: 'Owners - Property Portal'
    }
  },
  {
    path: '/maintenance',
    name: 'maintenance',
    component: () => import('../views/MaintenanceView.vue'),
    meta: {
      title: 'Maintenance - Property Portal'
    }
  },
  {
    path: '/leases',
    name: 'leases',
    component: () => import('../views/LeasesView.vue'),
    meta: {
      title: 'Leases - Property Portal'
    }
  },
  {
    path: '/payments',
    name: 'payments',
    component: () => import('../views/PaymentsView.vue'),
    meta: {
      title: 'Payments - Property Portal'
    }
  },
  {
    // Not found route - must be last
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('../views/NotFoundView.vue'),
    meta: {
      title: 'Page Not Found - Property Portal'
    }
  }
]

// Create router instance
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL || '/'),
  routes,
  scrollBehavior(to, from, savedPosition) {
    // Scroll to top on navigation, or restore saved position
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// Navigation guard for setting page titles
router.beforeEach((to, from, next) => {
  // Set page title
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router