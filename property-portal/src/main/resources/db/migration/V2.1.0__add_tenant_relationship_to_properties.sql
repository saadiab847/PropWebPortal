ALTER TABLE properties 
ADD COLUMN tenant_id BIGINT COMMENT 'Reference to the tenant who owns this property';

-- Create index for tenant_id (improves query performance for tenant-specific filtering)
CREATE INDEX idx_property_tenant_id ON properties(tenant_id);

-- Add foreign key constraint
ALTER TABLE properties
ADD CONSTRAINT fk_property_tenant
FOREIGN KEY (tenant_id) REFERENCES tenants(id)
ON DELETE RESTRICT
ON UPDATE CASCADE;

-- If there are existing properties, we can associate them with a default tenant
-- First, we need to ensure a default tenant exists
INSERT INTO tenants (
    tenant_code, 
    name, 
    email, 
    active
)
SELECT 
    'DEFAULT', 
    'Default Tenant',
    'admin@propertyportal.com',
    TRUE
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM tenants WHERE tenant_code = 'DEFAULT'
);

-- Now, associate existing properties with the default tenant
UPDATE properties p
SET p.tenant_id = (SELECT id FROM tenants WHERE tenant_code = 'DEFAULT')
WHERE p.tenant_id IS NULL;

-- Make tenant_id non-nullable after migration
ALTER TABLE properties
MODIFY COLUMN tenant_id BIGINT NOT NULL COMMENT 'Reference to the tenant who owns this property';