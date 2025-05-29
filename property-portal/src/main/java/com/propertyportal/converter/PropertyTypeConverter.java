package com.propertyportal.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.propertyportal.model.Property.PropertyType;

@Converter(autoApply = true)
public class PropertyTypeConverter implements AttributeConverter<PropertyType, String> {

    @Override
    public String convertToDatabaseColumn(PropertyType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase();
    }

    @Override
    public PropertyType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return PropertyType.valueOf(dbData.toUpperCase());
    }
}