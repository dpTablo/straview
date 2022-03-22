package com.dptablo.straview.dto.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListIntegerConverter implements AttributeConverter<List<Integer>, String> {
    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        try {
            String[] strings = dbData.substring(1, dbData.length() - 1).split(",");
            return Arrays.stream(strings)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch(Exception e) {
            return Collections.emptyList();
        }
    }
}
