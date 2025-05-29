package com.propertyportal.service;

import com.propertyportal.dto.PropertyDTO;
import com.propertyportal.model.Property;
import com.propertyportal.model.Property.PropertyType;
import com.propertyportal.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PropertyService {
    
    private final PropertyRepository propertyRepository;
    
    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
    
    public List<PropertyDTO> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<PropertyDTO> getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public List<PropertyDTO> getAvailableProperties() {
        return propertyRepository.findByIsAvailableTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PropertyDTO saveProperty(PropertyDTO propertyDTO) {
        Property property = convertToEntity(propertyDTO);
        return convertToDTO(propertyRepository.save(property));
    }
    
    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
    
    public List<PropertyDTO> getPropertiesByCity(String city) {
        return propertyRepository.findByCity(city).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<PropertyDTO> getPropertiesByType(PropertyType type) {
        return propertyRepository.findByPropertyType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Convert Property entity to PropertyDTO
    private PropertyDTO convertToDTO(Property property) {
        return new PropertyDTO(
            property.getId(),
            property.getTitle(),
            property.getDescription(),
            property.getPropertyType(),
            property.getPrice(),
            property.getAddress(),
            property.getCity(),
            property.getState(),
            property.getZipCode(),
            property.getBedrooms(),
            property.getBathrooms(),
            property.getSquareFootage(),
            property.getIsAvailable()
        );
    }
    
    // Convert PropertyDTO to Property entity
    private Property convertToEntity(PropertyDTO dto) {
        Property property = new Property();
        property.setId(dto.id());
        property.setTitle(dto.title());
        property.setDescription(dto.description());
        property.setPropertyType(dto.propertyType());
        property.setPrice(dto.price());
        property.setAddress(dto.address());
        property.setCity(dto.city());
        property.setState(dto.state());
        property.setZipCode(dto.zipCode());
        property.setBedrooms(dto.bedrooms());
        property.setBathrooms(dto.bathrooms());
        property.setSquareFootage(dto.squareFootage());
        property.setIsAvailable(dto.isAvailable());
        return property;
    }
}