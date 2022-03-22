package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.dto.enumtype.converter.StringListBooleanConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name="activity_stream_moving")
@DiscriminatorValue("moving")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityStreamMoving extends ActivityStream {
    @Column(name = "data")
    @Convert(converter = StringListBooleanConverter.class)
    @JsonProperty("data")
    private List<Boolean> data;

    @Builder
    public ActivityStreamMoving(
            ActivityStreamType type,
            SummaryActivity summaryActivity,
            Long originalSize,
            ActivityStreamResolution resolution,
            ActivityStreamType seriesType,
            List<Boolean> data
    ) {
        super(type, summaryActivity, originalSize, resolution, seriesType);
        this.data = data;
    }
}
