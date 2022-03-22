package com.dptablo.straview.dto.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListFloatConverter implements AttributeConverter<List<Float>, String> {
    @Override
    public String convertToDatabaseColumn(List<Float> attribute) {
        return attribute.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",", "[", "]"));
    }

    @Override
    public List<Float> convertToEntityAttribute(String dbData) {
        try {
            String[] strings = dbData.substring(1, dbData.length() - 1).split(",");
            return Arrays.stream(strings)
                    .map(Float::parseFloat)
                    .collect(Collectors.toList());
        } catch(Exception e) {
            return Collections.emptyList();
        }
    }
}
