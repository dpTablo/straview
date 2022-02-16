package com.dptablo.straview.dto.converter;

import com.dptablo.straview.dto.enumtype.Role;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class RoleSetToStringConverterTest {

    RoleSetToStringConverter converter = new RoleSetToStringConverter();

    @Test
    void convertToDatabaseColumn() {
        HashSet<Role> list = new HashSet<>();
        list.add(Role.ADMIN);
        list.add(Role.USER);

        assertThat(converter.convertToDatabaseColumn(list)).contains("USER");
        assertThat(converter.convertToDatabaseColumn(list)).contains("ADMIN");
    }

    @Test
    void convertToDatabaseColumn_empty() {
        HashSet<Role> list = new HashSet<>();
        assertThatCode(() -> converter.convertToDatabaseColumn(list)).doesNotThrowAnyException();
    }

    @Test
    void convertToEntityAttribute() {
        String value = "ADMIN,USER";

        Set<Role> roleSet = converter.convertToEntityAttribute(value);
        assertThat(roleSet).contains(Role.USER, Role.ADMIN);
        assertThat(roleSet.size()).isEqualTo(2);
    }

    @Test
    void convertToEntityAttribute_empty() {
        assertThatCode(() -> converter.convertToEntityAttribute("")).doesNotThrowAnyException();
        assertThatCode(() -> converter.convertToEntityAttribute(null)).doesNotThrowAnyException();
    }
}