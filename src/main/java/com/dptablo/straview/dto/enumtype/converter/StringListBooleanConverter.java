package com.dptablo.straview.dto.enumtype.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListBooleanConverter implements AttributeConverter<List<Boolean>, String> {
    @Override
    public String convertToDatabaseColumn(List<Boolean> attribute) {
        return attribute.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",", "[", "]"));
    }

    @Override
    public List<Boolean> convertToEntityAttribute(String dbData) {
        try {
            String[] strings = dbData.substring(1, dbData.length() - 1).split(",");
            return Arrays.stream(strings)
                    .map(Boolean::parseBoolean)
                    .collect(Collectors.toList());
        } catch(Exception e) {
            return Collections.emptyList();
        }
    }
}
