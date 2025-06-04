package com.propertyportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tenants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "tenant_code", nullable = false, unique = true)
    private String tenantCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 100)
    @Column(name = "organization_name")
    private String organizationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type")
    private OrganizationType organizationType;

    @Size(max = 100)
    @Column(name = "contact_person")
    private String contactPerson;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 20)
    @Column(name = "phone")
    private String phone;

    // Address fields
    @Size(max = 100)
    @Column(name = "address_line1")
    private String addressLine1;

    @Size(max = 100)
    @Column(name = "address_line2")
    private String addressLine2;

    @Size(max = 50)
    @Column(name = "city")
    private String city;

    @Size(max = 50)
    @Column(name = "state")
    private String state;

    @Size(max = 20)
    @Column(name = "zip_code")
    private String zipCode;

    @Size(max = 50)
    @Column(name = "country")
    private String country;

    // Customization fields
    @Size(max = 255)
    @Column(name = "logo_url")
    private String logoUrl;

    @Size(max = 7)
    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "timezone", columnDefinition = "VARCHAR(50) DEFAULT 'UTC'")
    private String timezone = "UTC";

    @Column(name = "locale", columnDefinition = "VARCHAR(10) DEFAULT 'en_US'")
    private String locale = "en_US";

    // Status fields
    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Size(max = 50)
    @Column(name = "subscription_plan")
    private String subscriptionPlan;

    @Size(max = 20)
    @Column(name = "subscription_status")
    private String subscriptionStatus;

    @Column(name = "trial_ends_at")
    private LocalDateTime trialEndsAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Property> properties = new HashSet<>();

    // Organization type enum
    public enum OrganizationType {
        INDIVIDUAL, COMPANY, PARTNERSHIP, NON_PROFIT
    }
}