CREATE TABLE tenants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Unique identifier code for the tenant',
    name VARCHAR(100) NOT NULL COMMENT 'Display name of the tenant',
    organization_name VARCHAR(100) COMMENT 'Legal name of the organization',
    organization_type ENUM('INDIVIDUAL', 'COMPANY', 'PARTNERSHIP', 'NON_PROFIT') COMMENT 'Type of organization',
    
    -- Contact information
    contact_person VARCHAR(100) COMMENT 'Primary contact person name',
    email VARCHAR(100) NOT NULL COMMENT 'Primary contact email',
    phone VARCHAR(20) COMMENT 'Primary contact phone',
    
    -- Address information
    address_line1 VARCHAR(100) COMMENT 'Street address line 1',
    address_line2 VARCHAR(100) COMMENT 'Street address line 2',
    city VARCHAR(50) COMMENT 'City',
    state VARCHAR(50) COMMENT 'State or province',
    zip_code VARCHAR(20) COMMENT 'Postal or zip code',
    country VARCHAR(50) COMMENT 'Country',
    
    -- Customization and settings
    logo_url VARCHAR(255) COMMENT 'URL to tenant logo image',
    primary_color VARCHAR(7) COMMENT 'Primary brand color in hex format',
    timezone VARCHAR(50) DEFAULT 'UTC' COMMENT 'Tenant default timezone',
    locale VARCHAR(10) DEFAULT 'en_US' COMMENT 'Tenant default locale',
    
    -- Status and timestamps
    active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Whether the tenant is active',
    subscription_plan VARCHAR(50) COMMENT 'Current subscription plan',
    subscription_status VARCHAR(20) COMMENT 'Status of the subscription',
    trial_ends_at TIMESTAMP NULL COMMENT 'When the trial period ends',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'When the tenant was created',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'When the tenant was last updated',
    
    -- Constraints
    CONSTRAINT uk_tenant_email UNIQUE (email),
    CONSTRAINT uk_tenant_code UNIQUE (tenant_code)
);

-- Create indexes for better performance
CREATE INDEX idx_tenant_name ON tenants(name);
CREATE INDEX idx_tenant_active ON tenants(active);
CREATE INDEX idx_tenant_subscription_status ON tenants(subscription_status);