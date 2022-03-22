package com.dptablo.straview.dto.enumtype.converter;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class StringListBooleanConverterTest {
    private final StringListBooleanConverter converter = new StringListBooleanConverter();

    @Test
    public void convertToDatabaseColumn() {
        List<Boolean> list = Arrays.asList(true, false, false, true);

        String result = converter.convertToDatabaseColumn(list);
        assertThat(result).isEqualTo("[true,false,false,true]");
    }

    @Test
    public void convertToEntityAttribute() {
        String value = "[true,false,false,true]";

        List<Boolean> booleanList = converter.convertToEntityAttribute(value);
        assertThat(booleanList.size()).isEqualTo(4);
        assertThat(booleanList.get(0)).isTrue();
        assertThat(booleanList.get(1)).isFalse();
        assertThat(booleanList.get(2)).isFalse();
        assertThat(booleanList.get(3)).isTrue();
    }
}