// Shared Navigation JavaScript for Bark Buddy

class BarkBuddyNavigation {
  constructor() {
    this.validPages = [
      'dashboard.html',
      'adoptFriend.html', 
      'lostfound.html',
      'mydog.html',
      'listings.html',
      'adminDashboard.html',
      'login.html',
      'signup.html',
      'index.html',
      '404.html',
      'setting.html'
    ];
    this.init();
  }

  init() {
    try {
      this.checkPageExists();
      this.checkAuthentication();
      
      // Ensure navigation only initializes once DOM is ready
      if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
          this.initializeNavigation();
        });
      } else {
        this.initializeNavigation();
      }
    } catch (error) {
      console.error('Error initializing navigation:', error);
      this.handleNavigationError();
    }
  }

  initializeNavigation() {
    try {
      this.preventSidebarDuplication();
      this.setActiveNavItem();
      this.addNavigationEventListeners();
      this.addHoverEffects();
      console.log('BarkBuddy Navigation initialized successfully');
    } catch (error) {
      console.error('Error in navigation initialization:', error);
      // Retry once after a short delay
      setTimeout(() => {
        try {
          this.initializeNavigation();
        } catch (retryError) {
          console.error('Navigation retry failed:', retryError);
          this.handleNavigationError();
        }
      }, 500);
    }
  }

  handleNavigationError() {
    // Create a minimal fallback navigation if main nav fails
    const fallbackNav = document.createElement('div');
    fallbackNav.innerHTML = `
      <div class="alert alert-warning position-fixed top-0 start-0 w-100" style="z-index: 9999;" role="alert">
        <i class="fas fa-exclamation-triangle"></i> 
        Navigation temporarily unavailable. <a href="dashboard.html" class="alert-link">Return to Dashboard</a>
        <button type="button" class="btn-close float-end" onclick="this.parentElement.remove()"></button>
      </div>
    `;
    document.body.insertBefore(fallbackNav, document.body.firstChild);
  }

  checkPageExists() {
    const currentPage = window.location.pathname.split('/').pop();
    if (currentPage && !this.validPages.includes(currentPage) && currentPage !== '') {
      // Redirect to 404 page for invalid pages
      window.location.href = '404.html';
    }
  }

  preventSidebarDuplication() {
    // Remove any duplicate sidebars that might exist
    const sidebars = document.querySelectorAll('.sidebar');
    if (sidebars.length > 1) {
      // Keep only the first sidebar
      for (let i = 1; i < sidebars.length; i++) {
        sidebars[i].remove();
      }
    }
  }

  checkAuthentication() {
    const token = localStorage.getItem('accessToken');
    const currentPage = window.location.pathname.split('/').pop();
    const publicPages = ['login.html', 'signup.html', 'index.html', '404.html'];
    
    if (!token && !publicPages.includes(currentPage)) {
      this.showNotification('Please log in first', 'error');
      setTimeout(() => {
        window.location.href = 'login.html';
      }, 2000);
    }
  }

  setActiveNavItem() {
    const currentPage = window.location.pathname.split('/').pop();
    const sidebarNavLinks = document.querySelectorAll('.sidebar .nav-link');
    
    sidebarNavLinks.forEach(link => {
      link.classList.remove('active');
      const href = link.getAttribute('href');
      
      // Match current page with navigation links
      if (href === currentPage || 
          (currentPage === 'dashboard.html' && href === 'dashboard.html') ||
          (currentPage === 'adoptFriend.html' && href === 'adoptFriend.html') ||
          (currentPage === 'lostfound.html' && href === 'lostfound.html') ||
          (currentPage === 'mydog.html' && href === 'mydog.html') ||
          (currentPage === 'listings.html' && href === 'listings.html') ||
          (currentPage === 'setting.html' && href === 'setting.html') ||
          (currentPage === 'adminDashboard.html' && href === 'adminDashboard.html')) {
        link.classList.add('active');
      }
    });
  }

  addNavigationEventListeners() {
    const sidebarNavLinks = document.querySelectorAll('.sidebar .nav-link');
    
    sidebarNavLinks.forEach(link => {
      // Remove any existing event listeners to prevent duplicates
      link.removeEventListener('click', this.handleNavClick);
      
      link.addEventListener('click', (e) => {
        const href = link.getAttribute('href');
        
        // Handle logout separately
        if (link.textContent.includes('Logout')) {
          e.preventDefault();
          this.logout();
          return;
        }
        
        // Don't prevent default for valid pages - let normal navigation work
        if (href && href !== '#' && this.validPages.includes(href)) {
          // Remove active class from all links
          sidebarNavLinks.forEach(l => l.classList.remove('active'));
          // Add active class to clicked link
          link.classList.add('active');
          
          // Add loading state for valid navigation
          this.showNavigationLoading(link);
          
          // Track navigation for analytics
          console.log('Navigation to:', href);
          return; // Allow default navigation
        }
        
        // Prevent navigation for invalid pages
        if (href && href !== '#' && !this.validPages.includes(href)) {
          e.preventDefault();
          this.showNotification('Page not found', 'error');
          setTimeout(() => {
            window.location.href = '404.html';
          }, 1000);
          return;
        }
      });
    });
  }

  addHoverEffects() {
    const sidebarNavLinks = document.querySelectorAll('.sidebar .nav-link');
    
    sidebarNavLinks.forEach(link => {
      link.addEventListener('mouseenter', function() {
        if (!this.classList.contains('active')) {
          this.style.transform = 'translateX(5px)';
        }
      });
      
      link.addEventListener('mouseleave', function() {
        if (!this.classList.contains('active')) {
          this.style.transform = 'translateX(0)';
        }
      });
    });
  }

  showNavigationLoading(link) {
    const originalText = link.innerHTML;
    link.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Loading...';
    
    setTimeout(() => {
      link.innerHTML = originalText;
    }, 800);
  }

    logout() {
    Swal.fire({
      title: 'Are you sure?',
      text: "You will be logged out from the system!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, logout'
    }).then((result) => {
      if (result.isConfirmed) {
        // Clear all stored data
        localStorage.removeItem('accessToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userEmail');

        // Show success alert
        Swal.fire({
          title: 'Logged Out!',
          text: 'You have been logged out successfully. 🐾',
          icon: 'success',
          timer: 1500,
          showConfirmButton: false
        }).then(() => {
          // Redirect after alert closes
          window.location.href = 'login.html';
        });
      }
    });
  }


  showNotification(message, type = 'success') {
    // Create notification if it doesn't exist
    let notification = document.getElementById('navigation-notification');
    if (!notification) {
      notification = document.createElement('div');
      notification.id = 'navigation-notification';
      notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 10px;
        color: white;
        opacity: 0;
        transform: translateY(-20px);
        transition: all 0.3s ease;
        z-index: 1001;
        font-weight: 600;
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        max-width: 300px;
      `;
      document.body.appendChild(notification);
    }
    
    notification.textContent = message;
    notification.className = `notification show ${type}`;
    notification.style.backgroundColor = type === 'success' ? '#1cc88a' : '#e74a3b';
    notification.style.opacity = '1';
    notification.style.transform = 'translateY(0)';
    
    setTimeout(() => {
      notification.style.opacity = '0';
      notification.style.transform = 'translateY(-20px)';
    }, 4000);
  }

  // Navigation helper functions
  navigateToAdopt() {
    this.navigateToPage('adoptFriend.html');
  }

  navigateToLostFound() {
    this.navigateToPage('lostfound.html');
  }

  navigateToDashboard() {
    this.navigateToPage('dashboard.html');
  }

  navigateToMyDogs() {
    this.navigateToPage('mydog.html');
  }

  navigateToSettings() {
    this.navigateToPage('setting.html');
  }

  navigateToPage(page) {
    if (this.validPages.includes(page)) {
      window.location.href = page;
    } else {
      this.showNotification('Page not found', 'error');
      setTimeout(() => {
        window.location.href = '404.html';
      }, 1000);
    }
  }

  // Error handling for missing pages
  handlePageError() {
    window.addEventListener('error', (e) => {
      console.error('Page error:', e);
      this.showNotification('Something went wrong', 'error');
    });

    // Handle 404 errors
    window.addEventListener('unhandledrejection', (e) => {
      console.error('Unhandled promise rejection:', e);
      if (e.reason && e.reason.status === 404) {
        window.location.href = '404.html';
      }
    });
  }

  // Method to refresh navigation (useful for dynamic page updates)
  refreshNavigation() {
    this.setActiveNavItem();
    this.addNavigationEventListeners();
    this.addHoverEffects();
    this.preventSidebarDuplication();
  }

  // Method to manually set active page
  setActivePage(pageName) {
    const sidebarNavLinks = document.querySelectorAll('.sidebar .nav-link');
    sidebarNavLinks.forEach(link => {
      link.classList.remove('active');
      const href = link.getAttribute('href');
      if (href === pageName) {
        link.classList.add('active');
      }
    });
  }
}

// Initialize navigation when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
  window.barkBuddyNav = new BarkBuddyNavigation();
});

// Export for use in other scripts
if (typeof module !== 'undefined' && module.exports) {
  module.exports = BarkBuddyNavigation;
}
