package com.propertyportal.dto;

import com.propertyportal.model.Property.PropertyType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Data Transfer Object for Property information")
public record PropertyDTO(
    @Schema(description = "Unique identifier for the property") 
    Long id,
    
    @Schema(description = "Title of the property", example = "Modern Apartment in Downtown", required = true)
    String title,
    
    @Schema(description = "Detailed description of the property", example = "Beautiful apartment with panoramic views...")
    String description,
    
    @Schema(description = "Type of property", example = "APARTMENT", required = true)
    PropertyType propertyType,
    
    @Schema(description = "Price of the property in dollars", example = "350000.00", required = true)
    BigDecimal price,
    
    @Schema(description = "Street address of the property", example = "123 Main St", required = true)
    String address,
    
    @Schema(description = "City where the property is located", example = "New York")
    String city,
    
    @Schema(description = "State/province where the property is located", example = "NY")
    String state,
    
    @Schema(description = "ZIP/Postal code of the property", example = "10001")
    String zipCode,
    
    @Schema(description = "Number of bedrooms", example = "3")
    Integer bedrooms,
    
    @Schema(description = "Number of bathrooms", example = "2")
    Integer bathrooms,
    
    @Schema(description = "Square footage of the property", example = "1500.0")
    Double squareFootage,
    
    @Schema(description = "Indicates if the property is available for sale/rent", example = "true", defaultValue = "true")
    Boolean isAvailable,


    @Schema(description = "Indicates if the property belongs to which tenant", example = "1", defaultValue = "0")
    Long tenant_id 
    
) {
    // Compact constructor with validation
    public PropertyDTO {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        if (tenant_id == null ) {
            throw new IllegalArgumentException("Tenant Id cannot be empty");
        }
    }
}
