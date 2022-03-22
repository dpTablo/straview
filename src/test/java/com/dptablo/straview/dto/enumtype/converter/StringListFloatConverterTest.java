package com.dptablo.straview.dto.enumtype.converter;

import com.dptablo.straview.dto.converter.StringListFloatConverter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class StringListFloatConverterTest {
    private final StringListFloatConverter converter = new StringListFloatConverter();

    @Test
    public void convertToDatabaseColumn() {
        List<Float> list = Arrays.asList(134.4f, 134.6f, 135.2f, 137.5f);
        String result = converter.convertToDatabaseColumn(list);
        assertThat(result).isEqualTo("[134.4,134.6,135.2,137.5]");
    }

    @Test
    public void pconvertToEntityAttribute() {
        String value = "[134.4,134.6,135.2,137.5]";
        List<Float> list = converter.convertToEntityAttribute(value);
        assertThat(list.size()).isEqualTo(4);
        assertThat(list.get(0)).isEqualTo(134.4f);
        assertThat(list.get(1)).isEqualTo(134.6f);
        assertThat(list.get(2)).isEqualTo(135.2f);
        assertThat(list.get(3)).isEqualTo(137.5f);
    }
}