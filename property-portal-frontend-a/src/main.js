import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import axios from './plugins/axios' // Import axios configuration

// Create Vue app
const app = createApp(App)

// Use plugins
app.use(router)
app.use(vuetify)

// Configure axios
app.config.globalProperties.$http = axios
axios.defaults.baseURL = 'http://localhost:8080'

// Mount app
app.mount('#app')
