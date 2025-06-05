package com.propertyportal.controller;

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
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant Management", description = "APIs for managing tenants in the property portal")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    @Operation(summary = "Create a new tenant", description = "Creates a new tenant with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tenant successfully created",
            content = @Content(schema = @Schema(implementation = Tenant.class))),
        @ApiResponse(responseCode = "400", description = "Invalid tenant data provided"),
        @ApiResponse(responseCode = "409", description = "Tenant with the same code or email already exists")
    })
    public ResponseEntity<Tenant> createTenant(
            @Valid @RequestBody Tenant tenant) {
        
        // Check if tenant with the same code or email already exists
        if (tenantService.existsByTenantCode(tenant.getTenantCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tenant code already in use");
        }
        
        if (tenantService.existsByEmail(tenant.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tenant email already in use");
        }
        
        Tenant createdTenant = tenantService.createTenant(tenant);
        return ResponseEntity
                .created(URI.create("/api/tenants/" + createdTenant.getId()))
                .body(createdTenant);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tenant by ID", description = "Returns a tenant based on the ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the tenant"),
        @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<Tenant> getTenantById(
            @Parameter(description = "ID of the tenant to retrieve") 
            @PathVariable Long id) {
        
        return tenantService.getTenantById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tenant not found with id: " + id));
    }
    
    @GetMapping("/code/{tenantCode}")
    @Operation(summary = "Get tenant by code", description = "Returns a tenant based on its unique code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the tenant"),
        @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<Tenant> getTenantByCode(
            @Parameter(description = "Code of the tenant to retrieve") 
            @PathVariable String tenantCode) {
        
        return tenantService.getTenantByCode(tenantCode)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tenant not found with code: " + tenantCode));
    }

    @GetMapping
    @Operation(summary = "List all tenants", description = "Returns a paginated list of all tenants")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the tenant list")
    public ResponseEntity<Page<Tenant>> getAllTenants(
            @PageableDefault(size = 20, sort = "name") Pageable pageable,
            @RequestParam(required = false) Boolean activeOnly) {
        
        Page<Tenant> tenants;
        if (activeOnly != null && activeOnly) {
            tenants = tenantService.getActiveTenants(pageable);
        } else {
            tenants = tenantService.getAllTenants(pageable);
        }
        
        return ResponseEntity.ok(tenants);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tenant", description = "Updates an existing tenant with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tenant successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid tenant data provided"),
        @ApiResponse(responseCode = "404", description = "Tenant not found"),
        @ApiResponse(responseCode = "409", description = "Email conflict with another tenant")
    })
    public ResponseEntity<Tenant> updateTenant(
            @Parameter(description = "ID of the tenant to update") 
            @PathVariable Long id,
            @Valid @RequestBody Tenant tenantDetails) {
        
        // Check if the updated email conflicts with another tenant
        if (tenantService.existsByEmail(tenantDetails.getEmail())) {
            tenantService.getTenantById(id).ifPresent(existingTenant -> {
                if (!existingTenant.getEmail().equals(tenantDetails.getEmail())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use by another tenant");
                }
            });
        }
        
        try {
            Tenant updatedTenant = tenantService.updateTenant(id, tenantDetails);
            return ResponseEntity.ok(updatedTenant);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tenant not found with id: " + id, e);
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update tenant status", description = "Activates or deactivates a tenant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tenant status successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid status value"),
        @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<Map<String, Boolean>> updateTenantStatus(
            @Parameter(description = "ID of the tenant to update") 
            @PathVariable Long id,
            @RequestParam boolean active) {
        
        boolean updated = active ? 
                tenantService.activateTenant(id) : 
                tenantService.deactivateTenant(id);
        
        return ResponseEntity.ok(Map.of("success", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tenant", description = "Deletes a tenant by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tenant successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Tenant not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete tenant with associated properties")
    })
    public ResponseEntity<Void> deleteTenant(
            @Parameter(description = "ID of the tenant to delete") 
            @PathVariable Long id) {
        
        try {
            tenantService.deleteTenant(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("constraint")) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, 
                        "Cannot delete tenant with associated properties", e);
            }
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tenant not found with id: " + id, e);
        }
    }
}