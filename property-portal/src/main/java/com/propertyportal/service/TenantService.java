package com.propertyportal.service;

import com.propertyportal.model.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TenantService {
    
    Tenant createTenant(Tenant tenant);
    
    Tenant updateTenant(Long id, Tenant tenantDetails);
    
    Optional<Tenant> getTenantById(Long id);
    
    Optional<Tenant> getTenantByCode(String tenantCode);
    
    Page<Tenant> getAllTenants(Pageable pageable);
    
    Page<Tenant> getActiveTenants(Pageable pageable);
    
    boolean deactivateTenant(Long id);
    
    boolean activateTenant(Long id);
    
    void deleteTenant(Long id);
    
    boolean existsByTenantCode(String tenantCode);
    
    boolean existsByEmail(String email);
}