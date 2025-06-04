package com.propertyportal.controller;

import com.propertyportal.exception.ResourceNotFoundException;
import com.propertyportal.model.Tenant;
import com.propertyportal.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant Management", description = "APIs for managing tenants in the property portal")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    @Operation(summary = "Create a new tenant", description = "Creates a new tenant with the provided information")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tenant successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid tenant data provided"),
        @ApiResponse(responseCode = "409", description = "Tenant with the same code or email already exists")
    })
    public ResponseEntity<?> createTenant(@Valid @RequestBody Tenant tenant) {
        // Check for duplicates
        if (tenantService.existsByTenantCode(tenant.getTenantCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Tenant with code " + tenant.getTenantCode() + " already exists"));
        }
        
        if (tenantService.existsByEmail(tenant.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Tenant with email " + tenant.getEmail() + " already exists"));
        }
        
        Tenant savedTenant = tenantService.createTenant(tenant);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTenant.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(savedTenant);
    }

    @GetMapping
    @Operation(summary = "Get all tenants", description = "Retrieves a paginated list of all tenants with optional filtering")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved tenants")
    public ResponseEntity<Page<Tenant>> getAllTenants(
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        
        Page<Tenant> tenants;
        if (active != null) {
            tenants = tenantService.getActiveTenants(pageable);
        } else {
            tenants = tenantService.getAllTenants(pageable);
        }
        
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tenant by ID", description = "Retrieves a specific tenant by their unique ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tenant", 
                    content = @Content(schema = @Schema(implementation = Tenant.class))),
        @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<Tenant> getTenantById(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable Long id) {
        
        return tenantService.getTenantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{tenantCode}")
    @Operation(summary = "Get tenant by code", description = "Retrieves a specific tenant by their unique code")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tenant"),
        @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<Tenant> getTenantByCode(
            @Parameter(description = "Tenant code", required = true)
            @PathVariable String tenantCode) {
        
        return tenantService.getTenantByCode(tenantCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tenant", description = "Updates an existing tenant with the provided information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tenant successfully updated"),
        @ApiResponse(responseCode = "404", description = "Tenant not found"),
        @ApiResponse(responseCode = "400", description = "Invalid tenant data provided"),
        @ApiResponse(responseCode = "409", description = "Tenant email conflicts with an existing tenant")
    })
    public ResponseEntity<?> updateTenant(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody Tenant tenantDetails) {
        
        try {
            // Check if email already exists for another tenant
            if (tenantService.existsByEmail(tenantDetails.getEmail())) {
                Tenant existingTenantWithEmail = tenantService.getTenantByCode(tenantDetails.getEmail())
                        .orElse(null);
                
                // If email exists for a different tenant, return conflict
                if (existingTenantWithEmail != null && !existingTenantWithEmail.getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(Map.of("error", "Email " + tenantDetails.getEmail() + " is already in use"));
                }
            }
            
            Tenant updatedTenant = tenantService.updateTenant(id, tenantDetails);
            return ResponseEntity.ok(updatedTenant);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate tenant", description = "Activates a tenant by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tenant successfully activated"),
        @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<?> activateTenant(@PathVariable Long id) {
        try {
            boolean activated = tenantService.activateTenant(id);
            if (activated) {
                return ResponseEntity.ok(Map.of("message", "Tenant activated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to activate tenant"));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate tenant", description = "Deactivates a tenant by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tenant successfully deactivated"),
        @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<?> deactivateTenant(@PathVariable Long id) {
        try {
            boolean deactivated = tenantService.deactivateTenant(id);
            if (deactivated) {
                return ResponseEntity.ok(Map.of("message", "Tenant deactivated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to deactivate tenant"));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tenant", description = "Deletes a tenant by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tenant successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Tenant not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete tenant with associated properties")
    })
    public ResponseEntity<?> deleteTenant(@PathVariable Long id) {
        try {
            tenantService.deleteTenant(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // This could be a DataIntegrityViolationException if tenant has properties
            Map<String, String> response = new HashMap<>();
            response.put("error", "Cannot delete tenant. Please remove all properties associated with this tenant first.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}