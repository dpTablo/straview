package com.dptablo.straview.dto.enumtype.converter;

import com.dptablo.straview.dto.Latlng;
import com.dptablo.straview.dto.converter.LatlngConverter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class LatlngConverterTest {
    private final LatlngConverter converter = new LatlngConverter();

    @Test
    public void convertToDatabaseColumn() {
        Latlng latlng = Latlng.builder()
                .latitude(51.50957107543945)
                .longitude(-0.07956345565617085)
                .build();

        String result = converter.convertToDatabaseColumn(latlng);

        assertThat(result).isEqualTo("[51.50957107543945,-0.07956345565617085]");
    }

    @Test
    public void convertToEntityAttribute() {
        String dbData = "[51.50957107543945,-0.07956345565617085]";

        Latlng latlng = converter.convertToEntityAttribute(dbData);

        assertThat(latlng.getLatitude()).isEqualTo(51.50957107543945);
        assertThat(latlng.getLongitude()).isEqualTo(-0.07956345565617085);
    }
}