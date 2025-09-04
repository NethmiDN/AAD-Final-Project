/**
 * BarkBuddy AI Chatbot Widget
 * A floating chatbot widget that can be embedded on any page
 */

class BarkBuddyChatbotWidget {
  constructor(options = {}) {
    this.options = {
      apiUrl: options.apiUrl || 'http://localhost:8080/api/chat',
      position: options.position || 'bottom-right',
      theme: options.theme || 'default',
      autoOpen: options.autoOpen || false,
      showWelcome: options.showWelcome !== false,
      ...options
    };
    
    this.isOpen = false;
    this.isTyping = false;
    this.messageHistory = [];
    
    this.init();
  }

  init() {
    this.createChatbotHTML();
    this.attachEventListeners();
    this.injectStyles();
    
    if (this.options.autoOpen) {
      this.openChat();
    }
    
    if (this.options.showWelcome) {
      this.showWelcomeMessage();
    }
    
    console.log('BarkBuddy Chatbot Widget initialized');
  }

  createChatbotHTML() {
    const chatbotContainer = document.createElement('div');
    chatbotContainer.id = 'barkbuddy-chatbot-widget';
    chatbotContainer.innerHTML = `
      <!-- Chatbot Toggle Button -->
      <div class="chatbot-toggle" id="chatbot-toggle">
        <div class="chatbot-icon">
          <i class="fas fa-dog"></i>
        </div>
        <div class="chatbot-close-icon" style="display: none;">
          <i class="fas fa-times"></i>
        </div>
        <div class="chatbot-notification" id="chatbot-notification" style="display: none;">
          <span>New message!</span>
        </div>
      </div>

      <!-- Chatbot Window -->
      <div class="chatbot-window" id="chatbot-window" style="display: none;">
        <div class="chatbot-header">
          <div class="chatbot-header-info">
            <div class="chatbot-avatar">
              <i class="fas fa-dog"></i>
            </div>
            <div class="chatbot-title">
              <h4>BarkBuddy AI</h4>
              <span class="chatbot-status">Online</span>
            </div>
          </div>
          <div class="chatbot-actions">
            <button class="chatbot-minimize" id="chatbot-minimize">
              <i class="fas fa-minus"></i>
            </button>
            <button class="chatbot-close" id="chatbot-close">
              <i class="fas fa-times"></i>
            </button>
          </div>
        </div>

        <div class="chatbot-messages" id="chatbot-messages">
          <div class="chatbot-welcome" id="chatbot-welcome">
            <div class="welcome-avatar">
              <i class="fas fa-paw"></i>
            </div>
            <div class="welcome-text">
              <h5>Woof! I'm BarkBuddy AI! 🐕</h5>
              <p>I'm here to help you with all your dog-related questions. Ask me about:</p>
              <ul>
                <li>Dog care and training</li>
                <li>Adoption advice</li>
                <li>Health and nutrition</li>
                <li>Breed information</li>
              </ul>
            </div>
          </div>
        </div>

        <div class="chatbot-typing" id="chatbot-typing" style="display: none;">
          <div class="typing-avatar">
            <i class="fas fa-dog"></i>
          </div>
          <div class="typing-dots">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>

        <div class="chatbot-input">
          <div class="input-container">
            <input type="text" id="chatbot-input" placeholder="Ask me about dogs..." maxlength="500">
            <button id="chatbot-send" class="send-button">
              <i class="fas fa-paper-plane"></i>
            </button>
          </div>
          <div class="quick-actions">
            <button class="quick-action" data-message="How do I train my puppy?">
              🐶 Training
            </button>
            <button class="quick-action" data-message="What's the best food for dogs?">
              🍖 Nutrition
            </button>
            <button class="quick-action" data-message="How do I adopt a dog?">
              💛 Adoption
            </button>
          </div>
        </div>
      </div>
    `;

    document.body.appendChild(chatbotContainer);
  }

  injectStyles() {
    if (document.getElementById('barkbuddy-chatbot-styles')) return;

    const styles = document.createElement('style');
    styles.id = 'barkbuddy-chatbot-styles';
    styles.textContent = `
      #barkbuddy-chatbot-widget {
        position: fixed;
        bottom: 20px;
        right: 20px;
        z-index: 10000;
        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      }

      .chatbot-toggle {
        width: 60px;
        height: 60px;
        background: linear-gradient(135deg, #2C2A7B, #FEC83C);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        box-shadow: 0 4px 20px rgba(44, 42, 123, 0.3);
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
      }

      .chatbot-toggle:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 25px rgba(44, 42, 123, 0.4);
      }

      .chatbot-toggle .chatbot-icon,
      .chatbot-toggle .chatbot-close-icon {
        color: white;
        font-size: 24px;
        transition: all 0.3s ease;
      }

      .chatbot-notification {
        position: absolute;
        top: -5px;
        right: -5px;
        background: #ff4757;
        color: white;
        border-radius: 15px;
        padding: 4px 8px;
        font-size: 10px;
        font-weight: bold;
        animation: pulse 2s infinite;
      }

      @keyframes pulse {
        0% { transform: scale(1); }
        50% { transform: scale(1.1); }
        100% { transform: scale(1); }
      }

      .chatbot-window {
        position: absolute;
        bottom: 80px;
        right: 0;
        width: 380px;
        height: 500px;
        background: white;
        border-radius: 15px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
        display: flex;
        flex-direction: column;
        overflow: hidden;
        animation: slideUp 0.3s ease-out;
      }

      @keyframes slideUp {
        from {
          opacity: 0;
          transform: translateY(20px);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
      }

      .chatbot-header {
        background: linear-gradient(135deg, #2C2A7B, #FEC83C);
        color: white;
        padding: 15px;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .chatbot-header-info {
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .chatbot-avatar {
        width: 35px;
        height: 35px;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 16px;
      }

      .chatbot-title h4 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
      }

      .chatbot-status {
        font-size: 12px;
        opacity: 0.9;
      }

      .chatbot-actions {
        display: flex;
        gap: 5px;
      }

      .chatbot-actions button {
        background: none;
        border: none;
        color: white;
        cursor: pointer;
        padding: 5px;
        border-radius: 3px;
        transition: background 0.2s ease;
      }

      .chatbot-actions button:hover {
        background: rgba(255, 255, 255, 0.2);
      }

      .chatbot-messages {
        flex: 1;
        overflow-y: auto;
        padding: 15px;
        background: #f8f9fa;
      }

      .chatbot-welcome {
        text-align: center;
        padding: 20px 0;
      }

      .welcome-avatar {
        width: 50px;
        height: 50px;
        background: linear-gradient(135deg, #2C2A7B, #FEC83C);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 15px;
        color: white;
        font-size: 20px;
      }

      .welcome-text h5 {
        color: #2C2A7B;
        margin-bottom: 10px;
      }

      .welcome-text p {
        color: #666;
        font-size: 14px;
        margin-bottom: 10px;
      }

      .welcome-text ul {
        text-align: left;
        max-width: 200px;
        margin: 0 auto;
        font-size: 13px;
        color: #666;
      }

      .chat-message {
        margin-bottom: 15px;
        animation: messageSlide 0.3s ease-out;
      }

      @keyframes messageSlide {
        from {
          opacity: 0;
          transform: translateY(10px);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
      }

      .message-user {
        display: flex;
        justify-content: flex-end;
      }

      .message-bot {
        display: flex;
        justify-content: flex-start;
        align-items: flex-start;
        gap: 8px;
      }

      .message-avatar {
        width: 25px;
        height: 25px;
        background: linear-gradient(135deg, #2C2A7B, #FEC83C);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 12px;
        flex-shrink: 0;
      }

      .message-content {
        max-width: 250px;
        padding: 10px 15px;
        border-radius: 15px;
        font-size: 14px;
        line-height: 1.4;
      }

      .message-user .message-content {
        background: linear-gradient(135deg, #667eea, #764ba2);
        color: white;
        border-bottom-right-radius: 5px;
      }

      .message-bot .message-content {
        background: white;
        color: #333;
        border: 1px solid #e0e0e0;
        border-bottom-left-radius: 5px;
      }

      .message-time {
        font-size: 11px;
        opacity: 0.7;
        margin-top: 5px;
        text-align: center;
      }

      .chatbot-typing {
        padding: 15px;
        display: flex;
        align-items: center;
        gap: 10px;
        background: #f8f9fa;
        border-top: 1px solid #e0e0e0;
      }

      .typing-avatar {
        width: 25px;
        height: 25px;
        background: linear-gradient(135deg, #2C2A7B, #FEC83C);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 12px;
      }

      .typing-dots {
        display: flex;
        gap: 4px;
      }

      .typing-dots span {
        width: 6px;
        height: 6px;
        background: #999;
        border-radius: 50%;
        animation: typing 1.4s infinite ease-in-out;
      }

      .typing-dots span:nth-child(2) {
        animation-delay: 0.2s;
      }

      .typing-dots span:nth-child(3) {
        animation-delay: 0.4s;
      }

      @keyframes typing {
        0%, 60%, 100% {
          transform: scale(0.8);
          opacity: 0.5;
        }
        30% {
          transform: scale(1);
          opacity: 1;
        }
      }

      .chatbot-input {
        background: white;
        border-top: 1px solid #e0e0e0;
        padding: 15px;
      }

      .input-container {
        display: flex;
        gap: 10px;
        margin-bottom: 10px;
      }

      .input-container input {
        flex: 1;
        border: 2px solid #e0e0e0;
        border-radius: 20px;
        padding: 10px 15px;
        font-size: 14px;
        outline: none;
        transition: border-color 0.3s ease;
      }

      .input-container input:focus {
        border-color: #667eea;
      }

      .send-button {
        width: 40px;
        height: 40px;
        background: linear-gradient(135deg, #667eea, #764ba2);
        border: none;
        border-radius: 50%;
        color: white;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
      }

      .send-button:hover {
        transform: scale(1.05);
      }

      .send-button:disabled {
        opacity: 0.6;
        cursor: not-allowed;
        transform: none;
      }

      .quick-actions {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
      }

      .quick-action {
        background: #f8f9fa;
        border: 1px solid #e0e0e0;
        border-radius: 15px;
        padding: 5px 10px;
        font-size: 12px;
        cursor: pointer;
        transition: all 0.2s ease;
        color: #666;
      }

      .quick-action:hover {
        background: #667eea;
        color: white;
        border-color: #667eea;
      }

      /* Scrollbar styling */
      .chatbot-messages::-webkit-scrollbar {
        width: 4px;
      }

      .chatbot-messages::-webkit-scrollbar-track {
        background: #f1f1f1;
      }

      .chatbot-messages::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 2px;
      }

      /* Mobile responsiveness */
      @media (max-width: 480px) {
        .chatbot-window {
          width: calc(100vw - 40px);
          height: calc(100vh - 100px);
          bottom: 80px;
          right: 20px;
          left: 20px;
        }

        #barkbuddy-chatbot-widget {
          right: 20px;
        }
      }
    `;

    document.head.appendChild(styles);
  }

  attachEventListeners() {
    const toggle = document.getElementById('chatbot-toggle');
    const window = document.getElementById('chatbot-window');
    const minimize = document.getElementById('chatbot-minimize');
    const close = document.getElementById('chatbot-close');
    const input = document.getElementById('chatbot-input');
    const sendBtn = document.getElementById('chatbot-send');
    const quickActions = document.querySelectorAll('.quick-action');

    toggle.addEventListener('click', () => this.toggleChat());
    minimize.addEventListener('click', () => this.closeChat());
    close.addEventListener('click', () => this.closeChat());
    
    input.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') this.sendMessage();
    });
    
    sendBtn.addEventListener('click', () => this.sendMessage());

    quickActions.forEach(action => {
      action.addEventListener('click', () => {
        const message = action.getAttribute('data-message');
        input.value = message;
        this.sendMessage();
      });
    });
  }

  toggleChat() {
    if (this.isOpen) {
      this.closeChat();
    } else {
      this.openChat();
    }
  }

  openChat() {
    const window = document.getElementById('chatbot-window');
    const toggle = document.getElementById('chatbot-toggle');
    const icon = toggle.querySelector('.chatbot-icon');
    const closeIcon = toggle.querySelector('.chatbot-close-icon');
    
    window.style.display = 'flex';
    icon.style.display = 'none';
    closeIcon.style.display = 'block';
    
    this.isOpen = true;
    
    // Focus on input
    setTimeout(() => {
      document.getElementById('chatbot-input').focus();
    }, 300);

    this.hideNotification();
  }

  closeChat() {
    const window = document.getElementById('chatbot-window');
    const toggle = document.getElementById('chatbot-toggle');
    const icon = toggle.querySelector('.chatbot-icon');
    const closeIcon = toggle.querySelector('.chatbot-close-icon');
    
    window.style.display = 'none';
    icon.style.display = 'block';
    closeIcon.style.display = 'none';
    
    this.isOpen = false;
  }

  async sendMessage() {
    const input = document.getElementById('chatbot-input');
    const message = input.value.trim();
    
    if (!message || this.isTyping) return;

    this.isTyping = true;
    input.disabled = true;
    document.getElementById('chatbot-send').disabled = true;

    // Add user message
    this.addMessage(message, 'user');
    input.value = '';

    // Hide welcome message
    this.hideWelcomeMessage();

    // Show typing indicator
    this.showTyping();

    try {
      const response = await fetch(this.options.apiUrl, {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify({ message: message })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      
      // Simulate typing delay
      setTimeout(() => {
        this.hideTyping();
        this.addMessage(data.response || 'Sorry, I couldn\'t process that request.', 'bot');
        
        // Re-enable input
        this.isTyping = false;
        input.disabled = false;
        document.getElementById('chatbot-send').disabled = false;
        input.focus();

        // Store message history
        this.messageHistory.push(
          { sender: 'user', message: message, timestamp: new Date() },
          { sender: 'bot', message: data.response, timestamp: new Date() }
        );

      }, 1000 + Math.random() * 1000);

    } catch (error) {
      console.error('Chatbot error:', error);
      this.hideTyping();
      
      let errorMessage = 'Sorry, I\'m having trouble right now. ';
      
      if (error.message.includes('Failed to fetch')) {
        errorMessage += 'Please make sure you\'re connected to the internet and our server is running.';
      } else {
        errorMessage += 'Please try again in a moment.';
      }
      
      this.addMessage(errorMessage, 'bot');
      
      // Re-enable input
      this.isTyping = false;
      input.disabled = false;
      document.getElementById('chatbot-send').disabled = false;
      input.focus();
    }
  }

  addMessage(message, sender) {
    const messagesContainer = document.getElementById('chatbot-messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `chat-message message-${sender}`;
    
    const time = new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
    
    if (sender === 'bot') {
      messageDiv.innerHTML = `
        <div class="message-avatar">
          <i class="fas fa-dog"></i>
        </div>
        <div>
          <div class="message-content">${message}</div>
          <div class="message-time">${time}</div>
        </div>
      `;
    } else {
      messageDiv.innerHTML = `
        <div>
          <div class="message-content">${message}</div>
          <div class="message-time">${time}</div>
        </div>
      `;
    }

    messagesContainer.appendChild(messageDiv);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
  }

  showTyping() {
    document.getElementById('chatbot-typing').style.display = 'flex';
    const messagesContainer = document.getElementById('chatbot-messages');
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
  }

  hideTyping() {
    document.getElementById('chatbot-typing').style.display = 'none';
  }

  showWelcomeMessage() {
    // Welcome message is shown by default in HTML
  }

  hideWelcomeMessage() {
    const welcomeMessage = document.getElementById('chatbot-welcome');
    if (welcomeMessage) {
      welcomeMessage.style.display = 'none';
    }
  }

  showNotification() {
    if (!this.isOpen) {
      const notification = document.getElementById('chatbot-notification');
      notification.style.display = 'block';
    }
  }

  hideNotification() {
    const notification = document.getElementById('chatbot-notification');
    notification.style.display = 'none';
  }

  // Public API methods
  open() {
    this.openChat();
  }

  close() {
    this.closeChat();
  }

  sendCustomMessage(message) {
    const input = document.getElementById('chatbot-input');
    input.value = message;
    this.sendMessage();
  }

  getMessageHistory() {
    return this.messageHistory;
  }

  clearHistory() {
    this.messageHistory = [];
    const messagesContainer = document.getElementById('chatbot-messages');
    messagesContainer.innerHTML = `
      <div class="chatbot-welcome" id="chatbot-welcome">
        <div class="welcome-avatar">
          <i class="fas fa-paw"></i>
        </div>
        <div class="welcome-text">
          <h5>Woof! I'm BarkBuddy AI! 🐕</h5>
          <p>I'm here to help you with all your dog-related questions. Ask me about:</p>
          <ul>
            <li>Dog care and training</li>
            <li>Adoption advice</li>
            <li>Health and nutrition</li>
            <li>Breed information</li>
          </ul>
        </div>
      </div>
    `;
  }
}

// Auto-initialize if script is loaded directly
if (typeof window !== 'undefined') {
  window.BarkBuddyChatbotWidget = BarkBuddyChatbotWidget;
  
  // Auto-initialize with default settings
  document.addEventListener('DOMContentLoaded', function() {
    if (!window.barkBuddyChatbot) {
      window.barkBuddyChatbot = new BarkBuddyChatbotWidget();
    }
  });
}

// Export for module systems
if (typeof module !== 'undefined' && module.exports) {
  module.exports = BarkBuddyChatbotWidget;
}
