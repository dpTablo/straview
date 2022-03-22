package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.dto.converter.StringListIntegerConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name="activity_stream_time")
@DiscriminatorValue("time")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityStreamTime extends ActivityStream {
    @Column(name = "data")
    @Convert(converter = StringListIntegerConverter.class)
    @JsonProperty("data")
    private List<Integer> data;

    @Builder
    public ActivityStreamTime(
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
