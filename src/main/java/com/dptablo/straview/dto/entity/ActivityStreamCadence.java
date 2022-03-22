package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.dto.enumtype.converter.StringListBooleanConverter;
import com.dptablo.straview.dto.enumtype.converter.StringListIntegerConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name="activity_stream_cadence")
@DiscriminatorValue("cadence")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityStreamCadence extends ActivityStream {
    @Column(name = "data")
    @Convert(converter = StringListIntegerConverter.class)
    @JsonProperty("data")
    private List<Integer> data;

    @Builder
    public ActivityStreamCadence(
            ActivityStreamType type,
            SummaryActivity summaryActivity,
            Long originalSize,
            ActivityStreamResolution resolution,
            ActivityStreamType seriesType,
            List<Integer> data
    ) {
        super(type, summaryActivity, originalSize, resolution, seriesType);
        this.data = data;
    }
}
