package com.propertyportal.service.impl;

import com.propertyportal.exception.ResourceNotFoundException;
import com.propertyportal.model.Tenant;
import com.propertyportal.repository.TenantRepository;
import com.propertyportal.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public Tenant createTenant(Tenant tenant) {
        // Additional validation or business logic can be added here
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public Tenant updateTenant(Long id, Tenant tenantDetails) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        
        // Update fields
        tenant.setName(tenantDetails.getName());
        tenant.setOrganizationName(tenantDetails.getOrganizationName());
        tenant.setOrganizationType(tenantDetails.getOrganizationType());
        tenant.setContactPerson(tenantDetails.getContactPerson());
        tenant.setEmail(tenantDetails.getEmail());
        tenant.setPhone(tenantDetails.getPhone());
        
        // Address fields
        tenant.setAddressLine1(tenantDetails.getAddressLine1());
        tenant.setAddressLine2(tenantDetails.getAddressLine2());
        tenant.setCity(tenantDetails.getCity());
        tenant.setState(tenantDetails.getState());
        tenant.setZipCode(tenantDetails.getZipCode());
        tenant.setCountry(tenantDetails.getCountry());
        
        // Customization fields
        tenant.setLogoUrl(tenantDetails.getLogoUrl());
        tenant.setPrimaryColor(tenantDetails.getPrimaryColor());
        tenant.setTimezone(tenantDetails.getTimezone());
        tenant.setLocale(tenantDetails.getLocale());
        
        // Status fields
        tenant.setSubscriptionPlan(tenantDetails.getSubscriptionPlan());
        tenant.setSubscriptionStatus(tenantDetails.getSubscriptionStatus());
        tenant.setTrialEndsAt(tenantDetails.getTrialEndsAt());
        
        return tenantRepository.save(tenant);
    }

    @Override
    public Optional<Tenant> getTenantById(Long id) {
        return tenantRepository.findById(id);
    }

    @Override
    public Optional<Tenant> getTenantByCode(String tenantCode) {
        return tenantRepository.findByTenantCode(tenantCode);
    }

    @Override
    public Page<Tenant> getAllTenants(Pageable pageable) {
        return tenantRepository.findAll(pageable);
    }

    @Override
    public Page<Tenant> getActiveTenants(Pageable pageable) {
        return tenantRepository.findByActive(true, pageable);
    }

    @Override
    @Transactional
    public boolean deactivateTenant(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        
        tenant.setActive(false);
        tenantRepository.save(tenant);
        return true;
    }

    @Override
    @Transactional
    public boolean activateTenant(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        
        tenant.setActive(true);
        tenantRepository.save(tenant);
        return true;
    }

    @Override
    @Transactional
    public void deleteTenant(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        
        // Additional business logic before deletion can be added here
        // e.g., check if tenant can be deleted (no properties, etc.)
        
        tenantRepository.delete(tenant);
    }

    @Override
    public boolean existsByTenantCode(String tenantCode) {
        return tenantRepository.existsByTenantCode(tenantCode);
    }

    @Override
    public boolean existsByEmail(String email) {
        return tenantRepository.existsByEmail(email);
    }
}