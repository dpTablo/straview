package com.dptablo.straview.dto.converter;

import com.dptablo.straview.dto.Latlng;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StringListLatlngConverterTest {
    private final StringListLatlngConverter converter = new StringListLatlngConverter();

    @Test
    public void convertToDatabaseColumn() {
        List<Latlng> list = new ArrayList<>();
        list.add(new Latlng(-11.633, 166.945));
        list.add(new Latlng(-11.634, 166.946));
        list.add(new Latlng(-11.635, 166.947));
        list.add(new Latlng(-11.636, 166.948));

        String result = converter.convertToDatabaseColumn(list);
        assertThat(result).isEqualTo("[[-11.633,166.945],[-11.634,166.946],[-11.635,166.947],[-11.636,166.948]]");
    }

    @Test
    public void convertToEntityAttribute() {
        String value = "[[-11.633,166.945],[-11.634,166.946],[-11.635,166.947],[-11.636,166.948]]";
        List<Latlng> latlngList = converter.convertToEntityAttribute(value);

        assertThat(latlngList.size()).isEqualTo(4);

        assertThat(latlngList.get(0).getLatitude()).isEqualTo(-11.633);
        assertThat(latlngList.get(0).getLongitude()).isEqualTo(166.945);

        assertThat(latlngList.get(1).getLatitude()).isEqualTo(-11.634);
        assertThat(latlngList.get(1).getLongitude()).isEqualTo(166.946);

        assertThat(latlngList.get(2).getLatitude()).isEqualTo(-11.635);
        assertThat(latlngList.get(2).getLongitude()).isEqualTo(166.947);

        assertThat(latlngList.get(3).getLatitude()).isEqualTo(-11.636);
        assertThat(latlngList.get(3).getLongitude()).isEqualTo(166.948);
    }
}