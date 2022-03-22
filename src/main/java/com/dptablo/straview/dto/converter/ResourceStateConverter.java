package com.dptablo.straview.dto.converter;

import com.dptablo.straview.dto.enumtype.ResourceState;

import javax.persistence.AttributeConverter;

public class ResourceStateConverter implements AttributeConverter<ResourceState, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ResourceState attribute) {
        return attribute.getValue();
    }

    @Override
    public ResourceState convertToEntityAttribute(Integer dbData) {
        return ResourceState.valueOf(dbData);
    }
}
