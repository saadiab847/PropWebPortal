import { API_CONFIG } from '@/config';

class WebSocketService {
  constructor() {
    this.client = null;
    this.isConnected = false;
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 5;
    this.reconnectTimeout = null;
    this.listeners = new Map();
  }

  connect() {
    return new Promise((resolve, reject) => {
      try {
        // Close existing connection if any
        if (this.client) {
          this.disconnect();
        }

        console.log(`Connecting to WebSocket: ${API_CONFIG.websocketUrl}`);
        this.client = new WebSocket(API_CONFIG.websocketUrl);

        // Setup event handlers
        this.client.onopen = () => {
          console.log('WebSocket connection established');
          this.isConnected = true;
          this.reconnectAttempts = 0;
          resolve(true);
        };

        this.client.onmessage = (event) => {
          this.handleMessage(event);
        };

        this.client.onerror = (error) => {
          console.error('WebSocket error:', error);
          reject(error);
        };

        this.client.onclose = (event) => {
          console.log(`WebSocket connection closed: ${event.code} ${event.reason}`);
          this.isConnected = false;
          this.handleReconnect();
        };
      } catch (error) {
        console.error('Failed to connect to WebSocket:', error);
        reject(error);
      }
    });
  }

  handleReconnect() {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      const delay = Math.min(1000 * Math.pow(2, this.reconnectAttempts), 30000);
      
      console.log(`Attempting to reconnect (${this.reconnectAttempts}/${this.maxReconnectAttempts}) in ${delay}ms`);
      
      clearTimeout(this.reconnectTimeout);
      this.reconnectTimeout = setTimeout(() => {
        this.connect().catch(err => {
          console.error('Reconnection failed:', err);
        });
      }, delay);
    } else {
      console.error('Maximum reconnection attempts reached');
    }
  }

  disconnect() {
    if (this.client && this.isConnected) {
      this.client.close(1000, 'Client disconnecting');
      this.isConnected = false;
    }
    clearTimeout(this.reconnectTimeout);
  }

  send(message) {
    if (!this.client || !this.isConnected) {
      console.error('Cannot send message: WebSocket not connected');
      return false;
    }

    try {
      const messageStr = typeof message === 'string' ? 
        message : JSON.stringify(message);
      this.client.send(messageStr);
      return true;
    } catch (error) {
      console.error('Failed to send message:', error);
      return false;
    }
  }

  handleMessage(event) {
    try {
      const data = JSON.parse(event.data);
      const messageType = data.type || 'unknown';
      
      // Notify all listeners registered for this message type
      if (this.listeners.has(messageType)) {
        this.listeners.get(messageType).forEach(callback => {
          try {
            callback(data);
          } catch (err) {
            console.error(`Error in message listener for ${messageType}:`, err);
          }
        });
      }
      
    } catch (error) {
      console.error('Failed to process message:', error);
    }
  }

  subscribe(messageType, callback) {
    if (!this.listeners.has(messageType)) {
      this.listeners.set(messageType, []);
    }
    this.listeners.get(messageType).push(callback);
    
    // Return an unsubscribe function
    return () => this.unsubscribe(messageType, callback);
  }

  unsubscribe(messageType, callback) {
    if (this.listeners.has(messageType)) {
      const callbacks = this.listeners.get(messageType);
      const index = callbacks.indexOf(callback);
      if (index !== -1) {
        callbacks.splice(index, 1);
      }
    }
  }
}

// Singleton instance
const websocketService = new WebSocketService();
export default websocketService;