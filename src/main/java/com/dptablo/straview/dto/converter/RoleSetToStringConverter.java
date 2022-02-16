package com.dptablo.straview.dto.converter;

import com.dptablo.straview.dto.enumtype.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class RoleSetToStringConverter implements AttributeConverter<Set<Role>, String> {
    @Override
    public String convertToDatabaseColumn(Set<Role> attribute) {
        return attribute.stream()
                .map(Role::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()) {
            return Collections.emptySet();
        }

        return Stream.of(dbData.split(",", -1))
                .filter(s -> s != null && !s.isEmpty())
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}
