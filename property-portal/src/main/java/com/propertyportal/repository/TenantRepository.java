package com.propertyportal.repository;

import com.propertyportal.model.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    
    Optional<Tenant> findByTenantCode(String tenantCode);
    
    Optional<Tenant> findByEmail(String email);
    
    Page<Tenant> findByActive(boolean active, Pageable pageable);
    
    boolean existsByTenantCode(String tenantCode);
    
    boolean existsByEmail(String email);

    Optional<Tenant> findById(long id);
    
}