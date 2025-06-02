module.exports = {
  // Enable proper handling of URLs
  publicPath: '/',
  
  devServer: {
    // Should match your running port
    port: 8084,
    // Fix history mode routing
    historyApiFallback: true,
    
    // Set up proxy for API calls
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        ws: true,
        changeOrigin: true
      }
    }
  }
}