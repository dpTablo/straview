package com.dptablo.straview.dto.converter;

import com.dptablo.straview.dto.Latlng;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LatlngConverter implements AttributeConverter<Latlng, String> {
    @Override
    public String convertToDatabaseColumn(Latlng attribute) {
        return String.format("[%s,%s]", attribute.getLatitude(), attribute.getLongitude());
    }

    @Override
    public Latlng convertToEntityAttribute(String dbData) {
        try {
            String values = dbData.substring(1, dbData.length() - 1);
            String[] split = values.split(",");

            return Latlng.builder()
                    .latitude(Double.parseDouble(split[0]))
                    .longitude(Double.parseDouble(split[1]))
                    .build();
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
