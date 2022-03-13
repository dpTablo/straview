package com.dptablo.straview.dto.converter;

import com.dptablo.straview.dto.enumtype.Role;
import com.dptablo.straview.dto.enumtype.converter.RoleSetToStringConverter;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class RoleSetToStringConverterTest {

    RoleSetToStringConverter converter = new RoleSetToStringConverter();

    @Test
    public void convertToDatabaseColumn() {
        HashSet<Role> list = new HashSet<>();
        list.add(Role.ADMIN);
        list.add(Role.USER);

        assertThat(converter.convertToDatabaseColumn(list)).contains("USER");
        assertThat(converter.convertToDatabaseColumn(list)).contains("ADMIN");
    }

    @Test
    public void convertToDatabaseColumn_empty() {
        HashSet<Role> list = new HashSet<>();
        assertThatCode(() -> converter.convertToDatabaseColumn(list)).doesNotThrowAnyException();
    }

    @Test
    public void convertToEntityAttribute() {
        String value = "ADMIN,USER";

        Set<Role> roleSet = converter.convertToEntityAttribute(value);
        assertThat(roleSet).contains(Role.USER, Role.ADMIN);
        assertThat(roleSet.size()).isEqualTo(2);
    }

    @Test
    public void convertToEntityAttribute_empty() {
        assertThatCode(() -> converter.convertToEntityAttribute("")).doesNotThrowAnyException();
        assertThatCode(() -> converter.convertToEntityAttribute(null)).doesNotThrowAnyException();
    }
}