package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.dto.enumtype.converter.StringListIntegerConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name="activity_stream_heartrate")
@DiscriminatorValue("heartrate")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityStreamHeartrate extends ActivityStream {
    @Column(name = "data")
    @Convert(converter = StringListIntegerConverter.class)
    @JsonProperty("data")
    private List<Integer> data;

    @Builder
    public ActivityStreamHeartrate(
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