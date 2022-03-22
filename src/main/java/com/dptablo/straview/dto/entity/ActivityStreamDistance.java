package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.dto.converter.StringListIntegerConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name="activity_stream_distance")
@DiscriminatorValue("distance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityStreamDistance extends ActivityStream {
    @Column(name = "data")
    @Convert(converter = StringListIntegerConverter.class)
    @JsonProperty("data")
    private List<Integer> data;

    @Builder
    public ActivityStreamDistance(
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
