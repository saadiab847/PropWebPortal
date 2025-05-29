-- Initial schema creation for properties
CREATE TABLE IF NOT EXISTS properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    property_type VARCHAR(50) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    bedrooms INT,
    bathrooms INT,
    square_footage DOUBLE,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert initial sample data
INSERT INTO properties (title, description, property_type, price, address, city, state, zip_code, bedrooms, bathrooms, square_footage, is_available)
VALUES 
('Modern Apartment in Downtown', 'Beautiful modern apartment in the heart of downtown with amazing city views.', 'APARTMENT', 350000.00, '123 Main St', 'New York', 'NY', '10001', 2, 2, 1200.0, true),
('Spacious Family Home', 'Perfect home for a growing family with a large backyard and modern amenities.', 'HOUSE', 450000.00, '456 Oak Avenue', 'Los Angeles', 'CA', '90001', 4, 3, 2500.0, true),
('Luxury Beachfront Condo', 'Stunning beachfront condo with panoramic ocean views and high-end finishes.', 'CONDO', 620000.00, '789 Beach Blvd', 'Miami', 'FL', '33101', 3, 2, 1800.0, true);