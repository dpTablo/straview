package com.dptablo.straview.dto.enumtype.converter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class StringListConverterTest {

    private final StringListConverter converter = new StringListConverter();

    @Test
    public void convertToDatabaseColumn() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        String result = converter.convertToDatabaseColumn(list);

        assertThat(result).isEqualTo("[1,2,3]");
    }

    @Test
    public void convertToEntityAttribute() {
        String value = "[1,2,3]";

        List<Integer> resultList = converter.convertToEntityAttribute(value);

        assertThat(resultList.size()).isEqualTo(3);
        assertThat(resultList.get(0)).isEqualTo(1);
        assertThat(resultList.get(1)).isEqualTo(2);
        assertThat(resultList.get(2)).isEqualTo(3);
    }
}