package com.propertyportal.repository;

import com.propertyportal.model.Property;
import com.propertyportal.model.Property.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByIsAvailableTrue();
    List<Property> findByCity(String city);
    List<Property> findByPropertyType(PropertyType type);
}

