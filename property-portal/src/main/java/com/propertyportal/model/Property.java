package com.propertyportal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.propertyportal.converter.PropertyTypeConverter;

@Schema(description = "Property entity representing real estate properties")
@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the property")
    private Long id;
    
    @Column(nullable = false)
    @Schema(description = "Title of the property", example = "Modern Apartment in Downtown", required = true)
    private String title;
    
    @Schema(description = "Detailed description of the property", example = "Beautiful apartment with panoramic views...")
    private String description;
    
    @Column(name = "property_type", nullable = false)
    @Convert(converter = PropertyTypeConverter.class)
    @Schema(description = "Type of property", example = "APARTMENT", required = true)
    private PropertyType propertyType;
    
    @Column(nullable = false)
    @Schema(description = "Price of the property in dollars", example = "350000.00", required = true)
    private BigDecimal price;
    
    @Column(nullable = false)
    @Schema(description = "Street address of the property", example = "123 Main St", required = true)
    private String address;
    
    @Schema(description = "City where the property is located", example = "New York")
    private String city;
    
    @Schema(description = "State/province where the property is located", example = "NY")
    private String state;
    
    @Column(name = "zip_code")
    @Schema(description = "ZIP/Postal code of the property", example = "10001")
    private String zipCode;
    
    @Schema(description = "Number of bedrooms", example = "3")
    private Integer bedrooms;
    
    @Schema(description = "Number of bathrooms", example = "2")
    private Integer bathrooms;
    
    @Column(name = "square_footage")
    @Schema(description = "Square footage of the property", example = "1500.0")
    private Double squareFootage;
    
    @Column(name = "is_available")
    @Schema(description = "Indicates if the property is available for sale/rent", example = "true", defaultValue = "true")
    private Boolean isAvailable = true;
    
    @Column(name = "created_at")
    @Schema(description = "Timestamp when the property was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the property was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
    
    public enum PropertyType {
        APARTMENT, HOUSE, CONDO, TOWNHOUSE, LAND, COMMERCIAL
    }
}