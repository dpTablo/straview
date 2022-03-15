package com.dptablo.straview.dto.enumtype.converter;

import com.dptablo.straview.dto.enumtype.ActivityStreamType;

import javax.persistence.AttributeConverter;

public class ActivityStreamTypeConverter implements AttributeConverter<ActivityStreamType, String> {
    @Override
    public String convertToDatabaseColumn(ActivityStreamType attribute) {
        return attribute.toString();
    }

    @Override
    public ActivityStreamType convertToEntityAttribute(String dbData) {
        return ActivityStreamType.valueOf(dbData);
    }
}
