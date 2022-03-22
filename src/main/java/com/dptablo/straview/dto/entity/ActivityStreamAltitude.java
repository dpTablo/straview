package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.dto.enumtype.converter.StringListFloatConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name="activity_stream_altitude")
@DiscriminatorValue("altitude")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityStreamAltitude extends ActivityStream {
    @Column(name = "data")
    @Convert(converter = StringListFloatConverter.class)
    @JsonProperty("data")
    private List<Float> data;

    @Builder
    public ActivityStreamAltitude(
            ActivityStreamType type,
            SummaryActivity summaryActivity,
            Long originalSize,
            ActivityStreamResolution resolution,
            ActivityStreamType seriesType,
            List<Float> data
    ) {
        super(type, summaryActivity, originalSize, resolution, seriesType);
        this.data = data;
    }
}
