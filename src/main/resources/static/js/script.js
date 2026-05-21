/**
 * Product Manager - JavaScript Utilities
 * Handles client-side interactions and validations
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips if Bootstrap tooltips are used
    initializeTooltips();
    
    // Handle form validation
    initializeFormValidation();
    
    // Initialize dynamic elements
    initializeDynamicElements();
});

/**
 * Initialize Bootstrap tooltips
 */
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Initialize form validation
 */
function initializeFormValidation() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            // Basic client-side validation can be added here
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
}

/**
 * Initialize dynamic elements
 */
function initializeDynamicElements() {
    // Add click handlers to table rows for better UX
    const tableRows = document.querySelectorAll('.product-row');
    tableRows.forEach(row => {
        row.addEventListener('click', function(e) {
            // Don't navigate if clicking on buttons
            if (e.target.closest('.btn') || e.target.closest('button')) {
                return;
            }
            
            // Navigate to product detail page
            const productId = row.querySelector('td:first-child').textContent.trim();
            if (productId) {
                window.location.href = '/products/' + productId;
            }
        });
    });
}

/**
 * Format currency value
 */
function formatCurrency(value) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(value);
}

/**
 * Format date and time
 */
function formatDateTime(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    }).format(date);
}

/**
 * Confirm action with modal
 */
function confirmAction(title, message, onConfirm) {
    const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
    document.getElementById('confirmModalTitle').textContent = title;
    document.getElementById('confirmModalMessage').textContent = message;
    
    const confirmBtn = document.getElementById('confirmModalBtn');
    confirmBtn.onclick = function() {
        modal.hide();
        onConfirm();
    };
    
    modal.show();
}

/**
 * Show delete confirmation for product
 */
function showDeleteModal(btn) {
    const productId = btn.getAttribute('data-product-id');
    const productName = btn.getAttribute('data-product-name');
    
    document.getElementById('productNameToDelete').textContent = productName;
    document.getElementById('deleteForm').action = '/products/' + productId + '/delete';
    
    new bootstrap.Modal(document.getElementById('deleteModal')).show();
}

/**
 * Debounce function for search inputs
 */
function debounce(func, delay) {
    let timeoutId;
    return function(...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => func.apply(this, args), delay);
    };
}

/**
 * Search products on input with debounce
 */
function setupSearchDebounce() {
    const searchInput = document.querySelector('input[name="search"]');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(function() {
            // Form submission can be triggered here if needed
        }, 500));
    }
}

/**
 * Add focus effects to form controls
 */
function setupFormFocusEffects() {
    const formControls = document.querySelectorAll('.form-control, .form-select');
    formControls.forEach(control => {
        control.addEventListener('focus', function() {
            this.closest('.mb-3')?.classList.add('focused');
        });
        
        control.addEventListener('blur', function() {
            this.closest('.mb-3')?.classList.remove('focused');
        });
    });
}

/**
 * Show loading state on buttons during form submission
 */
function setupFormSubmissionLoading() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function() {
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Loading...';
            }
        });
    });
}

/**
 * Handle numeric input for prices
 */
function formatPriceInput(input) {
    let value = input.value.replace(/[^\d.]/g, '');
    const parts = value.split('.');
    if (parts.length > 2) {
        value = parts[0] + '.' + parts.slice(1).join('');
    }
    if (parts[1] && parts[1].length > 2) {
        value = parts[0] + '.' + parts[1].slice(0, 2);
    }
    input.value = value;
}

/**
 * Initialize price input handlers
 */
function setupPriceInputs() {
    const priceInputs = document.querySelectorAll('input[id="price"]');
    priceInputs.forEach(input => {
        input.addEventListener('input', function() {
            formatPriceInput(this);
        });
    });
}

/**
 * Auto-hide alerts after 5 seconds
 */
function setupAutoHideAlerts() {
    const alerts = document.querySelectorAll('.alert:not(.alert-persistent)');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
}

// Initialize all features when DOM is ready
window.addEventListener('load', function() {
    setupFormFocusEffects();
    setupFormSubmissionLoading();
    setupPriceInputs();
    setupSearchDebounce();
    setupAutoHideAlerts();
});
