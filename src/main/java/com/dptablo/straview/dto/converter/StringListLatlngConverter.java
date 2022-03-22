package com.dptablo.straview.dto.converter;

import com.dptablo.straview.dto.Latlng;
import com.dptablo.straview.exception.StraviewErrorCode;
import com.dptablo.straview.exception.StraviewException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class StringListLatlngConverter implements AttributeConverter<List<Latlng>, String> {
    @Override
    public String convertToDatabaseColumn(List<Latlng> attribute) {
        return attribute.stream()
                .map(latlng -> "[" + latlng.getLatitude().toString() + "," + latlng.getLongitude().toString() + "]")
                .collect(Collectors.joining(",", "[", "]"));
    }

    @Override
    public List<Latlng> convertToEntityAttribute(String dbData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode arrayNode = objectMapper.readValue(dbData, ArrayNode.class);
            if(!arrayNode.isArray()) {
                throw new StraviewException(StraviewErrorCode.SERVER_PROCESSING_ERROR, "Latlng dbData invalid");
            }

            List<Latlng> list = new ArrayList<>();
            arrayNode.forEach(valueArrayNode -> {
                list.add(Latlng.builder()
                                .latitude(valueArrayNode.get(0).asDouble())
                                .longitude(valueArrayNode.get(1).asDouble())
                                .build());
            });
            return list;
        } catch(Throwable e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
