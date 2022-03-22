package com.dptablo.straview.dto.converter;

import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;

import javax.persistence.AttributeConverter;

public class ActivityStreamResolutionConverter implements AttributeConverter<ActivityStreamResolution, String> {
    @Override
    public String convertToDatabaseColumn(ActivityStreamResolution attribute) {
        return attribute.getValue();
    }

    @Override
    public ActivityStreamResolution convertToEntityAttribute(String dbData) {
        return ActivityStreamResolution.valueOf(dbData);
    }
}
