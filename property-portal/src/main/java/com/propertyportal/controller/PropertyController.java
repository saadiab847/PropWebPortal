package com.propertyportal.controller;

import com.propertyportal.dto.PropertyDTO;
import com.propertyportal.model.Property.PropertyType;
import com.propertyportal.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@Tag(name = "Property", description = "Property management APIs")
public class PropertyController {
    
    private final PropertyService propertyService;
    
    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all properties", description = "Retrieves a list of all properties in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved properties",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = PropertyDTO.class)))
    })
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get property by ID", description = "Retrieves a property by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the property",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = PropertyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Property not found", 
                    content = @Content)
    })
    public ResponseEntity<PropertyDTO> getPropertyById(
            @Parameter(description = "Unique identifier of the property", required = true) 
            @PathVariable Long id) {
        return propertyService.getPropertyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get available properties", description = "Retrieves a list of all available properties")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved available properties",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = PropertyDTO.class)))
    })
    public ResponseEntity<List<PropertyDTO>> getAvailableProperties() {
        return ResponseEntity.ok(propertyService.getAvailableProperties());
    }
    
    @GetMapping(value = "/city/{city}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get properties by city", description = "Retrieves properties located in the specified city")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved properties by city",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = PropertyDTO.class)))
    })
    public ResponseEntity<List<PropertyDTO>> getPropertiesByCity(
            @Parameter(description = "City name to search for", required = true) 
            @PathVariable String city) {
        return ResponseEntity.ok(propertyService.getPropertiesByCity(city));
    }
    
    @GetMapping(value = "/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get properties by type", description = "Retrieves properties of the specified type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved properties by type",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = PropertyDTO.class)))
    })
    public ResponseEntity<List<PropertyDTO>> getPropertiesByType(
            @Parameter(description = "Property type to search for", required = true) 
            @PathVariable PropertyType type) {
        return ResponseEntity.ok(propertyService.getPropertiesByType(type));
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create property", description = "Adds a new property to the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Property successfully created",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = PropertyDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid property data", 
                    content = @Content)
    })
    public ResponseEntity<PropertyDTO> createProperty(
            @Parameter(description = "Property data for creation", required = true) 
            @Valid @RequestBody PropertyDTO propertyDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.saveProperty(propertyDTO));
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update property", description = "Updates an existing property by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Property successfully updated",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = PropertyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Property not found", 
                    content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid property data", 
                    content = @Content)
    })
    public ResponseEntity<PropertyDTO> updateProperty(
            @Parameter(description = "Unique identifier of the property", required = true) 
            @PathVariable Long id, 
            @Parameter(description = "Updated property data", required = true) 
            @Valid @RequestBody PropertyDTO propertyDTO) {
        return propertyService.getPropertyById(id)
                .map(existing -> {
                    // Create a new DTO with the same ID
                    var updatedDTO = new PropertyDTO(
                        id,
                        propertyDTO.title(),
                        propertyDTO.description(),
                        propertyDTO.propertyType(),
                        propertyDTO.price(),
                        propertyDTO.address(),
                        propertyDTO.city(),
                        propertyDTO.state(),
                        propertyDTO.zipCode(),
                        propertyDTO.bedrooms(),
                        propertyDTO.bathrooms(),
                        propertyDTO.squareFootage(),
                        propertyDTO.isAvailable(),
                        propertyDTO.tenant_id()
                    );
                    return ResponseEntity.ok(propertyService.saveProperty(updatedDTO));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete property", description = "Deletes a property by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Property successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Property not found", 
                    content = @Content)
    })
    public ResponseEntity<Void> deleteProperty(
            @Parameter(description = "Unique identifier of the property", required = true) 
            @PathVariable Long id) {
        return propertyService.getPropertyById(id)
                .map(property -> {
                    propertyService.deleteProperty(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}