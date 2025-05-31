import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import './plugins/axios' // Import axios configuration

// Create Vue app
const app = createApp(App)

// Use plugins
app.use(router)
app.use(vuetify)

// Mount app
app.mount('#app')
